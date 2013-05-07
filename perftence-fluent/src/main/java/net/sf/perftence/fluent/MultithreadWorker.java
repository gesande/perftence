package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.AllowedExceptions;
import net.sf.perftence.CustomInvocationReporter;
import net.sf.perftence.Executable;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerfTestFailureFactory;
import net.sf.perftence.Startable;
import net.sf.perftence.TimerScheduler;
import net.sf.perftence.TimerSpec;
import net.sf.perftence.reporting.CustomFailureReporter;
import net.sf.perftence.reporting.Duration;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.TestSummaryLogger;
import net.sf.perftence.setup.PerformanceTestSetup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MultithreadWorker implements Startable {

    private static final Logger LOG = LoggerFactory
            .getLogger(MultithreadWorker.class);

    private final InvocationRunner runner;
    private final TestRuntimeReporter reporter;
    private final PerformanceTestSetup setUp;
    private final TimerScheduler timerScheduler;
    private final LatencyProvider latencyProvider;
    private final AllowedExceptions allowedExceptions;
    private final PerformanceRequirementValidator requirementValidator;
    private final TestSummaryLogger overallSummaryLogger;
    private final TestSummaryLogger intermediateSummaryLogger;
    private final List<CustomInvocationReporter> customReporters;
    private final List<CustomFailureReporter> customFailureReporters;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final PerfTestFailureFactory perfTestFailureFactory;

    private Invocation[] runnables;

    public MultithreadWorker(
            final TestRuntimeReporter reporter,
            final InvocationRunner runner,
            final PerformanceTestSetup setUp,
            final LatencyProvider latencyProvider,
            final AllowedExceptions allowedExceptions,
            final PerformanceRequirementValidator requirementValidator,
            final TestSummaryLogger overAllSummaryLogger,
            final TestSummaryLogger intermediateSummaryLogger,
            final LatencyFactory latencyFactory,
            final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder,
            final PerfTestFailureFactory perfTestFailureFactory) {
        this.reporter = reporter;
        this.runner = runner;
        this.setUp = setUp;
        this.latencyProvider = latencyProvider;
        this.allowedExceptions = allowedExceptions;
        this.latencyFactory = latencyFactory;
        this.allowedExceptionOccurredMessageBuilder = allowedExceptionOccurredMessageBuilder;
        this.requirementValidator = requirementValidator;
        this.overallSummaryLogger = overAllSummaryLogger;
        this.intermediateSummaryLogger = intermediateSummaryLogger;
        this.customReporters = new ArrayList<CustomInvocationReporter>();
        this.customFailureReporters = new ArrayList<CustomFailureReporter>();
        this.timerScheduler = new TimerScheduler();
        this.perfTestFailureFactory = perfTestFailureFactory;
    }

    public MultithreadWorker executable(final Executable original) {
        this.runnables = createRunnables(original);
        return this;
    }

    private PerformanceTestSetup setUp() {
        return this.setUp;
    }

    private Invocation[] createRunnables(final Executable original) {
        log().info("Creating runnables for setup {}\n{}", id(),
                setUp().toString());
        final MeasurableExecutable measurableExecutable = asMeasurable(original);
        if (totalThreads() > 0 && setUp().invocations() > 0) {
            return threadBased(setUp(), measurableExecutable);
        }
        if (setUp().duration() > 0 && totalThreads() > 0) {
            log().info("This seems to be duration based test");
            return durationBased(measurableExecutable, setUp().duration());
        }
        throw newPerfTestFailure("Invalid test setup " + setUp().toString()
                + "...check it out!");
    }

    private Invocation[] threadBased(final PerformanceTestSetup setUp,
            final MeasurableExecutable measurable) {
        log().info("This seems to be thread based test");
        final int invocationsPerThread = invocationsPerThread(setUp);
        return invocationsFor(measurable, invocationsPerThread,
                extraInvocations(setUp, invocationsPerThread));
    }

    private static int invocationsPerThread(final PerformanceTestSetup setUp) {
        return (setUp.invocations() / setUp.threads());
    }

    private int extraInvocations(final PerformanceTestSetup setUp,
            final int invocationsPerThread) {
        return setUp.invocations() - (invocationsPerThread * totalThreads());
    }

    private Invocation[] invocationsFor(
            final MeasurableExecutable adaptedExecutable,
            final int invocationsPerThread, final int extraInvocations) {
        final List<Invocation> invocations = new ArrayList<Invocation>();
        final Invocation[] invocationList = invocationBased(adaptedExecutable,
                invocationsPerThread, totalThreads());
        for (final Invocation invocation : invocationList) {
            invocations.add(invocation);
        }
        checkForExtraThreads(adaptedExecutable, extraInvocations, invocations);
        return invocations.toArray(new Invocation[invocations.size()]);
    }

    private void checkForExtraThreads(
            final MeasurableExecutable adaptedExecutable,
            final int extraInvocations, final List<Invocation> invocations) {
        if (extraInvocations > 0) {
            addExtraThreads(adaptedExecutable, extraInvocations, invocations);
        }
    }

    private void addExtraThreads(final MeasurableExecutable adaptedExecutable,
            final int extraInvocations, final List<Invocation> invocations) {
        informAboutExtraThread(extraInvocations);
        final Invocation[] extra = invocationBased(adaptedExecutable,
                extraInvocations, 1);
        for (final Invocation invocation : extra) {
            invocations.add(invocation);
        }
    }

    private static void informAboutExtraThread(final int extraInvocations) {
        log().info("Need to have extra thread with {} invocations.",
                extraInvocations);
    }

    private Invocation[] durationBased(final MeasurableExecutable executable,
            final int duration) {
        final Invocation runnables[] = new Invocation[totalThreads()];
        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new DurationBased(executable, duration);
        }
        return runnables;
    }

    final class DurationBased extends Invocation {

        private final Executable executable;
        private final int duration;
        private boolean shouldInterruptThreads = true;

        public DurationBased(final MeasurableExecutable executable,
                final int duration) {
            this.duration = duration;
            this.executable = executable;
        }

        @Override
        public void execute() {
            final long start = System.currentTimeMillis();
            final long endTime = start + this.duration;
            do {
                doExecute();
            } while (System.currentTimeMillis() < endTime);
        }

        private void doExecute() {
            try {
                this.shouldInterruptThreads = true;
                this.executable.execute();
            } catch (Throwable t) {
                handleException(t);
                interruptThreads();
            }
        }

        @Override
        protected void handleException(final Throwable t) {
            reportInvocationFailure(t);
            this.shouldInterruptThreads = false;
            if (t instanceof Exception) {
                if (!allowedExceptions().isAllowed((Exception) t)) {
                    reportTestFailure(t);
                } else {
                    log().warn(allowedExceptionOccurredMessage(t));
                }
            } else {
                reportTestFailure(t);
            }
        }

        private void reportTestFailure(final Throwable t) {
            log().error("Error running the test case: " + id(), t);
            this.shouldInterruptThreads = true;
            runner().testFailed(t);
        }

        @Override
        protected void interruptThreads() {
            if (shouldInterruptThreads()) {
                MultithreadWorker.this.interruptThreads();
            }
        }

        private boolean shouldInterruptThreads() {
            return this.shouldInterruptThreads;
        }
    }

    private String allowedExceptionOccurredMessage(final Throwable t) {
        return this.allowedExceptionOccurredMessageBuilder
                .allowedExceptionOccurredMessage(t, id());
    }

    private int totalThreads() {
        return setUp().threads();
    }

    private void scheduleTimer(final TimerSpec spec) {
        timerScheduler().schedule(spec);
    }

    private void reportThroughput() {
        reportThroughput(latencyProvider().currentDuration(), latencyProvider()
                .currentThroughput());
    }

    private void reportThroughput(final long currentDuration,
            final double currentThroughput) {
        reporter().throughput(currentDuration, currentThroughput);
    }

    private MeasurableExecutable asMeasurable(final Executable executable) {
        return new MeasurableExecutable(executable);
    }

    final class MeasurableExecutable implements Executable {

        private Executable executable;

        public MeasurableExecutable(final Executable original) {
            this.executable = original;
        }

        private Executable executable() {
            return this.executable;
        }

        @Override
        public void execute() throws Exception {
            final long callStart = System.nanoTime();
            executable().execute();
            final int latency = newLatency(callStart);
            addSample(latency);
            checkMaxRequirement(latency);
            reportLatency(latency);
        }

    }

    private int newLatency(final long callStart) {
        return this.latencyFactory.newLatency(callStart);
    }

    private void checkMaxRequirement(final int latency) {
        requirements().checkRuntimeLatency(id(), latency);
    }

    private PerformanceRequirementValidator requirements() {
        return this.requirementValidator;
    }

    private void addSample(final int latency) {
        latencyProvider().addSample(latency);
    }

    private void reportLatency(final int latency) {
        reporter().latency(latency);
        for (final CustomInvocationReporter reporter : customLatencyReporters()) {
            reporter.latency(latency);
        }
    }

    private Invocation[] invocationBased(final MeasurableExecutable executable,
            final int invocationsPerThread, final int threads) {
        final Invocation runnables[] = new Invocation[threads];
        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new InvocationBased(executable, invocationsPerThread);
        }
        return runnables;
    }

    final class InvocationBased extends Invocation {

        private final Executable executable;
        private final int invocationsPerThread;
        private boolean shouldInterruptThreads = true;

        public InvocationBased(final MeasurableExecutable executable,
                final int invocationsPerThread) {
            this.executable = executable;
            this.invocationsPerThread = invocationsPerThread;
        }

        private int invocationsPerThread() {
            return this.invocationsPerThread;
        }

        private Executable executable() {
            return this.executable;
        }

        @Override
        public void execute() {
            for (int i = 0; i < invocationsPerThread(); i++) {
                doExecute();
            }
        }

        private void doExecute() {
            try {
                this.shouldInterruptThreads = true;
                executable().execute();
            } catch (Throwable t) {
                handleException(t);
                interruptThreads();
            }
        }

        @Override
        protected void handleException(final Throwable t) {
            reportInvocationFailure(t);
            this.shouldInterruptThreads = false;
            if (t instanceof Exception) {
                if (!allowedExceptions().isAllowed((Exception) t)) {
                    reportTestFailure(t);
                } else {
                    log().warn(allowedExceptionOccurredMessage(t));
                }
            } else {
                reportTestFailure(t);
            }
        }

        private void reportTestFailure(final Throwable t) {
            log().error("Error running the test case: " + id(), t);
            this.shouldInterruptThreads = true;
            runner().testFailed(t);
        }

        @Override
        protected void interruptThreads() {
            if (shouldInterruptThreads()) {
                MultithreadWorker.this.interruptThreads();
            }
        }

        private boolean shouldInterruptThreads() {
            return this.shouldInterruptThreads;
        }
    }

    private AllowedExceptions allowedExceptions() {
        return this.allowedExceptions;
    }

    private void interruptThreads() {
        runner().interruptThreads();
    }

    private InvocationRunner runner() {
        return this.runner;
    }

    @Override
    public void start() {
        try {
            startRunning();
            run();
            stopRunning();
            printOverallSummary();
            checkRequirements();
            newSummary();
        } finally {
            runner().finished(id());
        }
    }

    private void printOverallSummary() {
        overallSummaryLogger().printSummary(id());
    }

    private TestSummaryLogger overallSummaryLogger() {
        return this.overallSummaryLogger;
    }

    private void checkRequirements() {
        checkRequirements(latencyProvider().duration());
        log().info("Passed performance requirement check.");
    }

    private void newSummary() {
        log().info("Summary time...");
        reporter().summary(id(), latencyProvider().duration(),
                latencyProvider().sampleCount(), latencyProvider().startTime());
    }

    private void run() {
        runner().run(runnables());
    }

    private void stopRunning() {
        stopCounter();
        stopScheduledTimers();
        log().info("Finished running {}", id());
    }

    private void stopCounter() {
        latencyProvider().stop();
        log().info("Latency counter stopped.");
        log().debug("Latency counter raw data {}", latencyProvider().toString());
    }

    private void stopScheduledTimers() {
        timerScheduler().stop();
    }

    private TimerScheduler timerScheduler() {
        return this.timerScheduler;
    }

    private void startRunning() {
        log().info("Start running {}", id());
        scheduleTimers();
        latencyProvider().start();
    }

    private void scheduleTimers() {
        scheduleThroughputTimer();
        scheduleIntermediateSummaryTimer();
        if (setUp().duration() > 0) {
            scheduleDurationBasedCheckTimer();
        }
    }

    private void scheduleDurationBasedCheckTimer() {
        scheduleTimer(new TimerSpec() {
            @Override
            public TimerTask task() {
                return new TimerTask() {
                    @Override
                    public void run() {
                        if (latencyProvider().currentDuration() > setUp()
                                .duration() + Duration.seconds(5)) {
                            log().error(
                                    "Execution time exceeded the duration given in the test setup, interrupting the test threads...");
                            runner().interruptThreads();
                        }
                    }

                };
            }

            @Override
            public int period() {
                return 1000;
            }

            @Override
            public String name() {
                return "duration-based-check-timer";
            }

            @Override
            public int delay() {
                return 1000;
            }
        });
    }

    private void scheduleThroughputTimer() {
        scheduleTimer(new TimerSpec() {
            @Override
            public TimerTask task() {
                return new TimerTask() {
                    @Override
                    public void run() {
                        reportThroughput();
                    }
                };
            }

            @Override
            public int period() {
                return 500;
            }

            @Override
            public String name() {
                return "throughput-reporting";
            }

            @Override
            public int delay() {
                return 1000;
            }
        });
    }

    private void scheduleIntermediateSummaryTimer() {
        scheduleTimer(new TimerSpec() {
            @Override
            public TimerTask task() {
                return new TimerTask() {
                    @Override
                    public void run() {
                        intermediateSummaryLogger().printSummary(id());
                    }
                };
            }

            @Override
            public int period() {
                return 1000;
            }

            @Override
            public String name() {
                return "intermediate-summary";
            }

            @Override
            public int delay() {
                return 1000;
            }
        });
    }

    private TestSummaryLogger intermediateSummaryLogger() {
        return this.intermediateSummaryLogger;
    }

    @Override
    public String id() {
        return runner().id();
    }

    private void checkRequirements(final long elapsedTime) {
        requirements().checkAllRequirements(id(), elapsedTime);
    }

    private LatencyProvider latencyProvider() {
        return this.latencyProvider;
    }

    private Invocation[] runnables() {
        return this.runnables;
    }

    private static Logger log() {
        return LOG;
    }

    public Startable startable() {
        return new Startable() {
            @Override
            public String id() {
                return MultithreadWorker.this.id();
            }

            @Override
            public void start() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MultithreadWorker.this.start();
                    }
                }, "perftence-test-startable-" + id()).start();
            }
        };
    }

    public boolean includeInvocationGraph() {
        return reporter().includeInvocationGraph();
    }

    private RuntimeException newPerfTestFailure(final String message) {
        return perfTestFailureFactory().newPerfTestFailure(message);
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

    public MultithreadWorker customInvocationReporters(
            final CustomInvocationReporter... customReporters) {
        for (CustomInvocationReporter reporter : customReporters) {
            customLatencyReporters().add(reporter);
        }
        return this;
    }

    private List<CustomInvocationReporter> customLatencyReporters() {
        return this.customReporters;
    }

    private TestRuntimeReporter reporter() {
        return this.reporter;
    }

    public MultithreadWorker customFailureReporter(
            CustomFailureReporter... customFailureReporters) {
        for (CustomFailureReporter custom : customFailureReporters) {
            customFailureReporters().add(custom);
        }
        return this;
    }

    private void reportInvocationFailure(final Throwable t) {
        reporter().invocationFailed(t);
        for (final CustomFailureReporter custom : customFailureReporters()) {
            custom.more(t);
        }
    }

    private List<CustomFailureReporter> customFailureReporters() {
        return this.customFailureReporters;
    }
}