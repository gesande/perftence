package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.AllowedExceptions;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.PerformanceTestSetupPojo;
import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.Startable;
import net.sf.perftence.TimerScheduler;
import net.sf.perftence.TimerSpec;
import net.sf.perftence.reporting.FailedInvocations;
import net.sf.perftence.reporting.FailedInvocationsFactory;
import net.sf.perftence.reporting.InvocationReporter;
import net.sf.perftence.reporting.InvocationReporterFactory;
import net.sf.perftence.reporting.summary.SummaryAppender;
import net.sf.perftence.reporting.summary.TestSummaryLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public final class TestBuilder implements RunnableAdapter, Startable {

    private final static Logger LOG = LoggerFactory
            .getLogger(TestBuilder.class);

    private final ActiveThreads activeThreads;
    private final String name;
    private final List<TestTaskCategory> latencyGraphFor;
    private final LatencyProvider latencyProvider;
    private final TestFailureNotifierDecorator failureNotifier;
    private final TimerScheduler timerScheduler;
    private final CategorySpecificLatencies categorySpecificLatencies;
    private final TaskScheduleDifferences taskScheduleDifferences;
    private final StorageForThreadsRunningCurrentTasks storageForThreadsRunningCurrentTasks;
    private final ScheduledTasks scheduledTasks;
    private final List<SummaryAppender> customSummaryAppenders;
    private final List<TestAgent> agents = new ArrayList<TestAgent>();
    private final LatencyVsConcurrentTasks runningTasks;
    private final AllowedExceptions allowedExceptions;
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final SummaryBuilderFactory summaryBuilderFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;

    private InvocationReporter overallReporter;
    private TestTaskSchedulingService schedulingService;
    private int workers = -1;
    private int workerWaitTime = 100;
    private int intermediateStatisticsInterval = 1000;
    private int throughputInterval = 1000;
    private TestSummaryLogger intermediateSummaryLogger;
    private int throughputRange = 2500;
    private int invocationRange = 500;

    TestBuilder(
            final String name,
            final TestFailureNotifierDecorator failureNotifier,
            final SummaryBuilderFactory summaryBuilderFactory,
            final FailedInvocationsFactory failedInvocationsFactory,
            final LatencyFactory latencyFactory,
            final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder) {
        this.name = name;
        this.failureNotifier = failureNotifier;
        this.failedInvocationsFactory = failedInvocationsFactory;
        this.summaryBuilderFactory = summaryBuilderFactory;
        this.latencyFactory = latencyFactory;
        this.allowedExceptionOccurredMessageBuilder = allowedExceptionOccurredMessageBuilder;
        this.latencyGraphFor = Collections
                .synchronizedList(new ArrayList<TestTaskCategory>());
        this.latencyProvider = new LatencyProvider();
        this.timerScheduler = new TimerScheduler();
        this.activeThreads = new ActiveThreads();
        this.scheduledTasks = new ScheduledTasks();
        this.categorySpecificLatencies = new CategorySpecificLatencies(this);
        this.taskScheduleDifferences = TaskScheduleDifferences.instance(name());
        this.storageForThreadsRunningCurrentTasks = StorageForThreadsRunningCurrentTasks
                .newStorage(name());
        this.customSummaryAppenders = new ArrayList<SummaryAppender>();
        this.runningTasks = LatencyVsConcurrentTasks.instance(name());
        this.allowedExceptions = new AllowedExceptions();
        log().info("TestBuilder {} created.", name());
    }

    InvocationReporter defaultInvocationReporter(
            final LatencyProvider latencyProvider, final int threads) {
        return InvocationReporterFactory.newDefaultInvocationReporter(
                latencyProvider, true, setup(0, threads, 0),
                newFailedInvocations());
    }

    @Override
    public String id() {
        return name();
    }

    private FailedInvocations newFailedInvocations() {
        return failedInvocationsFactory().newInstance();
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    /**
     * @param workers
     *            The number of workers for running test tasks, default value is
     *            the amount of agents;
     */
    public TestBuilder workers(final int workers) {
        this.workers = workers;
        return this;
    }

    public TestBuilder workerWaitTime(final int waitTimeInMillis) {
        this.workerWaitTime = waitTimeInMillis;
        return this;
    }

    public TestBuilder intermediateStatisticsInterval(
            final int intermediateStatisticsInterval) {
        this.intermediateStatisticsInterval = intermediateStatisticsInterval;
        return this;
    }

    /**
     * The test agents to be run by the framework
     */
    public TestBuilder agents(final Collection<TestAgent> agents) {
        addAgents(agents);
        return this;
    }

    private void newIntermediateSummaryBuilder() {
        this.intermediateSummaryLogger = summaryBuilderFactory()
                .intermediateSummaryBuilder(latencyProvider(), activeThreads(),
                        scheduledTasks());
        log().debug("Intermediate summary builder created.");
    }

    private SummaryBuilderFactory summaryBuilderFactory() {
        return this.summaryBuilderFactory;
    }

    private void createOverallReporter() {
        this.overallReporter = defaultInvocationReporter(latencyProvider(),
                workerThreads());
        log().debug("Overall reporter created.");
    }

    private int workerThreads() {
        return workers() > 0 ? workers() : agents().size();
    }

    private void newSchedulingService() {
        this.schedulingService = javaConcurrentStuffService();
        log().debug("Scheduling service created");
    }

    private TestTaskSchedulingService taskProviderService() {
        return SchedulingServiceFactory.newBasedOnTaskProvider(
                inMillis(workerWaitTime()), this, workerThreads(),
                failureNotifier());
    }

    private TestTaskSchedulingService javaConcurrentStuffService() {
        return SchedulingServiceFactory.newBasedOnJavaConcurrentStuff(this,
                workerThreads(), scheduledTasks());
    }

    private int workerWaitTime() {
        return this.workerWaitTime;
    }

    private int workers() {
        return this.workers;
    }

    private static Time inMillis(final long time) {
        return TimeSpecificationFactory.inMillis(time);
    }

    private void addAgents(final Collection<TestAgent> agents) {
        agents().clear();
        agents().addAll(agents);
        log().debug("Added '{}' agents.", agents().size());
    }

    public TestBuilder latencyGraphFor(final TestTaskCategory... categories) {
        for (final TestTaskCategory category : categories) {
            newCategorySpecificReporter(category);
            latencyGraphFor().add(category);
            log().debug("Added invocation reporter for {}", category);
        }
        reportCategorySpecificLatencies();
        return this;
    }

    public TestBuilder latencyGraphForAll() {
        latencyGraphFor().add(new TestTaskCategory() {
            @Override
            public String name() {
                return "all";
            }
        });
        latencyForAll();
        return this;
    }

    private void reportCategorySpecificLatencies() {
        categorySpecificLatencies().reportCategorySpecificLatencies();
    }

    private void newCategorySpecificReporter(TestTaskCategory category) {
        categorySpecificLatencies().newCategorySpecificReporter(category);
    }

    private void latencyForAll() {
        categorySpecificLatencies().latencyForAll();
    }

    @Override
    public void start() {
        log().info("Starting agent based test: {}", name());
        warmUp();
        try {
            run();
        } finally {
            stopRunning();
            log().info("Finished agent based test: {}", name());
        }
    }

    private void run() {
        log().info("Running {} until no scheduled tasks...", name());
        schedulingService().run();
        log().info("No more scheduled tasks for {}.", name());
    }

    private void warmUp() {
        log().info("Warming up...");
        newSchedulingService();
        createOverallReporter();
        newIntermediateSummaryBuilder();
        scheduleTimers();
        startLatencyCounter();
        startAdapters();
        scheduleFirstTasks();
        log().info("Warmed up.");
    }

    private void scheduleFirstTasks() {
        try {
            schedulingService().scheduleFirstTasks(agents());
        } catch (ScheduleFailedException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopRunning() {
        log().info("Finished all tasks for {}.", name());
        stopLatencyCounter();
        categorySpecificLatencies().stop();
        shutdownExecutor();
        stopTimers();
        categorySpecificSummary();
        doOverallSummary();
        resetCurrentThreads();
    }

    private void shutdownExecutor() {
        schedulingService().shutdown();
    }

    private void startAdapters() {
        categorySpecificLatencies().startAdapters();
    }

    private void categorySpecificSummary() {
        categorySpecificLatencies().summaryTime();
    }

    private void resetCurrentThreads() {
        activeThreads().reset();
    }

    private void startLatencyCounter() {
        latencyProvider().start();
        log().info("Latency counter started.");
    }

    private void doOverallSummary() {
        log().info("Creating overall summary...");
        doSummary(latencyProvider().duration(), latencyProvider().sampleCount());
        log().info("Overall summary done.");
    }

    private void stopLatencyCounter() {
        latencyProvider().stop();
        log().info("Latency counter stopped.");
    }

    private void stopTimers() {
        timerScheduler().stop();
    }

    private void scheduleTimers() {
        timerScheduler().schedule(
                intermediateStatisticsTimer(intermediateStatisticsInterval()));
        timerScheduler().schedule(throughputTimer(throughputInterval()));
        log().info("All timers scheduled.");
    }

    private TimerSpec throughputTimer(final int throughputInterval) {
        return new TimerSpec() {

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
                return throughputInterval;
            }

            @Override
            public String name() {
                return "throughput-reporting";
            }

            @Override
            public int delay() {
                return 1000;
            }
        };
    }

    private int throughputInterval() {
        return this.throughputInterval;
    }

    private int intermediateStatisticsInterval() {
        return this.intermediateStatisticsInterval;
    }

    private TimerSpec intermediateStatisticsTimer(final int period) {
        return new TimerSpec() {
            @Override
            public TimerTask task() {
                return new TimerTask() {
                    @Override
                    public void run() {
                        intermediateSummaryLogger().printSummary(
                                TestBuilder.this.name());
                    }
                };
            }

            @Override
            public int period() {
                return period;
            }

            @Override
            public String name() {
                return "intermediate-statistics";
            }

            @Override
            public int delay() {
                return 1000;
            }
        };
    }

    private StorageForThreadsRunningCurrentTasks threadsRunningCurrentTasks() {
        return this.storageForThreadsRunningCurrentTasks;
    }

    private void doSummary(final long duration, final long sampleCount) {
        printOverallSummary();
        newSummary(name() + "-overall-statistics", duration, sampleCount,
                latencyProvider().startTime());
    }

    private void printOverallSummary() {
        overAllSummaryBuilder().printSummary(name());
    }

    private void newSummary(final String name, final long duration,
            final long sampleCount, long startTime) {
        overallReporter().summary(name, duration, sampleCount, startTime);
    }

    private TestSummaryLogger overAllSummaryBuilder() {
        return summaryBuilderFactory().overallSummaryBuilder(latencyProvider());
    }

    private PerformanceTestSetup setup(final int duration, final int threads,
            final int sampleCount) {
        final PerformanceTestSetupBuilder testSetupBuilder = PerformanceTestSetupPojo
                .builder()
                .duration(duration)
                .threads(threads)
                .invocations(sampleCount)
                .invocationRange(invocationRange())
                .throughputRange(throughputRange())
                .summaryAppender(
                        threadsRunningCurrentTasks().summaryAppender(),
                        taskScheduleDifferencies().summaryAppender());
        for (final SummaryAppender appender : customSummaryAppenders()) {
            testSetupBuilder.summaryAppender(appender);
        }
        return testSetupBuilder.graphWriter(
                threadsRunningCurrentTasks().graphWriter(),
                taskScheduleDifferencies().graphWriter(),
                runningTasks().graphWriter()).build();
    }

    class TaskSpecificTestReporter implements TestTaskReporter {

        private final long callStart;

        public TaskSpecificTestReporter(final long callStart) {
            this.callStart = callStart;
        }

        /**
         * Returns the time spent in task so far in milliseconds.
         */
        @Override
        public Time timeSpentSoFar() {
            final int timeSpent = newLatency(this.callStart);
            return new Time() {

                @Override
                public TimeUnit timeUnit() {
                    return TimeUnit.MILLISECONDS;
                }

                @Override
                public long time() {
                    return timeSpent;
                }

                @Override
                public String toString() {
                    return "TimeSpecification [time()=" + time()
                            + ", timeUnit()=" + timeUnit() + "]";
                }
            };
        }
    }

    @Override
    public Runnable adapt(final TestTask task, final long timeItWasScheduled) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    final int activeTasks = activeThreads().more();
                    storeThreadsRunningCurrentTasks(activeTasks);
                    final long callStart = System.nanoTime();
                    task.run(new TaskSpecificTestReporter(callStart));
                    final int latency = newLatency(callStart);
                    addSample(latency, task.category());
                    reportWhenDifference(callStart - timeItWasScheduled);
                    reportLatencyForRunningTasks(activeTasks, latency);
                } catch (Throwable t) {
                    handleFailure(task, t);
                } finally {
                    storeThreadsRunningCurrentTasks(activeThreads().less());
                    markDone(task);
                }
            }
        };
    }

    private void reportThroughput() {
        overallReporter().throughput(latencyProvider().currentDuration(),
                latencyProvider().currentThroughput());
    }

    private void reportLatencyForRunningTasks(final int activeTasks,
            final int latency) {
        runningTasks().report(activeTasks, latency);
    }

    private void reportWhenDifference(final long difference) {
        taskScheduleDifferencies().report(difference);
    }

    private void storeThreadsRunningCurrentTasks(final int threads) {
        threadsRunningCurrentTasks().store(latencyProvider().currentDuration(),
                threads);
    }

    private synchronized void handleFailure(final TestTask task,
            final Throwable cause) {
        final boolean allowed = isAllowed(cause);
        if (!allowed) {
            log().error(taskFailedMessage(task), cause);
        }
        schedulingService().handleFailure(task, cause);
        categorySpecificLatencies().reportFailure(task.category(), cause);
        overallReporter().invocationFailed(cause);
        if (!allowed) {
            failureNotifier().testFailed(cause);
            finishedWithError(task, cause);
        } else {
            log().warn(allowedExceptionOccurredMessage(cause));
        }
    }

    private String allowedExceptionOccurredMessage(final Throwable cause) {
        return this.allowedExceptionOccurredMessageBuilder
                .allowedExceptionOccurredMessage(cause, name());
    }

    private boolean isAllowed(final Throwable cause) {
        return cause instanceof Exception ? allowedExceptions().isAllowed(
                (Exception) cause) : false;
    }

    private static void finishedWithError(final TestTask task,
            final Throwable cause) {
        log().error("Finished task {} with error {} !", task, cause);
    }

    private static String taskFailedMessage(final TestTask task) {
        return new StringBuilder("Task ").append(task.toString())
                .append(" failed ").toString();
    }

    private synchronized void markDone(final TestTask task) {
        schedulingService().markDone(task);
    }

    private synchronized void addSample(final int latency,
            final TestTaskCategory category) {
        latencyProvider().addSample(latency);
        overallReporter().latency(latency);
        categorySpecificLatencies().reportCategorySpecificLatency(latency,
                category);
    }

    private int newLatency(final long callStart) {
        return latencyFactory().newLatency(callStart);
    }

    private LatencyFactory latencyFactory() {
        return this.latencyFactory;
    }

    private int throughputRange() {
        return this.throughputRange;
    }

    private int invocationRange() {
        return this.invocationRange;
    }

    private List<SummaryAppender> customSummaryAppenders() {
        return this.customSummaryAppenders;
    }

    public String name() {
        return this.name;
    }

    private InvocationReporter overallReporter() {
        return this.overallReporter;
    }

    private LatencyProvider latencyProvider() {
        return this.latencyProvider;
    }

    private TestFailureNotifierDecorator failureNotifier() {
        return this.failureNotifier;
    }

    private List<TestTaskCategory> latencyGraphFor() {
        return this.latencyGraphFor;
    }

    private TimerScheduler timerScheduler() {
        return this.timerScheduler;
    }

    private TestSummaryLogger intermediateSummaryLogger() {
        return this.intermediateSummaryLogger;
    }

    private List<TestAgent> agents() {
        return this.agents;
    }

    private CategorySpecificLatencies categorySpecificLatencies() {
        return this.categorySpecificLatencies;
    }

    private TestTaskSchedulingService schedulingService() {
        return this.schedulingService;
    }

    private TaskScheduleDifferences taskScheduleDifferencies() {
        return this.taskScheduleDifferences;
    }

    private ActiveThreads activeThreads() {
        return this.activeThreads;
    }

    private ScheduledTasks scheduledTasks() {
        return this.scheduledTasks;
    }

    private LatencyVsConcurrentTasks runningTasks() {
        return this.runningTasks;
    }

    private static Logger log() {
        return LOG;
    }

    public TestBuilder summaryAppender(final SummaryAppender... appenders) {
        for (final SummaryAppender appender : appenders) {
            addAppender(appender);
        }
        return this;
    }

    private void addAppender(SummaryAppender appender) {
        customSummaryAppenders().add(appender);
    }

    public TestBuilder invocationRange(final int invocationRange) {
        this.invocationRange = invocationRange;
        return this;
    }

    public TestBuilder throughputRange(final int throughputRange) {
        this.throughputRange = throughputRange;
        return this;
    }

    public TestBuilder allow(final Class<? extends Exception> allowed) {
        allowedExceptions().allow(allowed);
        return this;
    }

    private AllowedExceptions allowedExceptions() {
        return this.allowedExceptions;
    }

}
