package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.AllowedErrors;
import net.sf.perftence.AllowedExceptionOrErrorOccurredMessageBuilder;
import net.sf.perftence.AllowedExceptions;
import net.sf.perftence.CustomInvocationReporter;
import net.sf.perftence.LastSecondStatistics;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.LatencyProviderFactory;
import net.sf.perftence.PerftenceRuntimeException;
import net.sf.perftence.Startable;
import net.sf.perftence.TimerScheduler;
import net.sf.perftence.TimerSpec;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.graph.LastSecondThroughput;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.graph.ScatterPlotAdapterProvider;
import net.sf.perftence.reporting.CustomFailureReporter;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilder;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FailedInvocations;
import net.sf.perftence.reporting.summary.FailedInvocationsFactory;
import net.sf.perftence.reporting.summary.LastSecondFailures;
import net.sf.perftence.reporting.summary.LastSecondIntermediateStatisticsProvider;
import net.sf.perftence.reporting.summary.SummaryAppender;
import net.sf.perftence.reporting.summary.SummaryConsumer;
import net.sf.perftence.reporting.summary.SummaryToCsv;
import net.sf.perftence.reporting.summary.SummaryToCsv.CsvSummary;
import net.sf.perftence.reporting.summary.TestSummaryLogger;
import net.sf.perftence.setup.PerformanceTestSetup;
import net.sf.perftence.setup.PerformanceTestSetupPojo;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

public final class TestBuilder implements RunnableAdapter, Startable, ReporterFactoryForCategorySpecificLatencies {

    private final static Logger LOG = LoggerFactory.getLogger(TestBuilder.class);

    private final LastSecondThroughput lastSecondThroughput;

    private final ActiveThreads activeThreads;
    private final String name;
    private final LatencyProvider latencyProvider;
    private final TestFailureNotifierDecorator failureNotifier;
    private final TimerScheduler timerScheduler;
    private final CategorySpecificLatencies categorySpecificLatencies;
    private final TaskScheduleDifferences taskScheduleDifferences;
    private final StorageForThreadsRunningCurrentTasks storageForThreadsRunningCurrentTasks;
    private final ScheduledTasks scheduledTasks;
    private final List<SummaryAppender> customSummaryAppenders;
    private final List<TestAgent> agents = new ArrayList<>();
    private final LatencyVsConcurrentTasks runningTasks;
    private final AllowedExceptions allowedExceptions;
    private final AllowedErrors allowedErrors;

    private final FailedInvocationsFactory failedInvocationsFactory;
    private final SummaryBuilderFactory summaryBuilderFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;

    private TestRuntimeReporter overallReporter;
    private TestTaskSchedulingService schedulingService;
    private int workers = -1;
    @SuppressWarnings("unused")
    private int workerWaitTime = 100;
    private int intermediateStatisticsInterval = 1000;
    private int throughputInterval = 1000;
    private TestSummaryLogger intermediateSummaryLogger;
    private int throughputRange = 2500;
    private int invocationRange = 500;

    private final List<CustomInvocationReporter> customLatencyReporters;
    private final List<CustomFailureReporter> customFailureReporters;
    private final FailedInvocations failedInvocations;
    private final LastSecondStatistics lastSecondStats;
    private final LastSecondFailures lastSecondFailures;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final SchedulingServiceFactory schedulingServiceFactory;
    private final CategorySpecificLatenciesConfigurator categorySpecificLatenciesConfigurator;
    private final LastSecondFailuresGraphWriter lastSecondFailureGraphWriter;
    private final TestRuntimeReporterFactory testRuntimeReporterFactory;
    private final SummaryConsumer summaryConsumer;

    private boolean includeInvocationGraph = true;
    private boolean includeThreadsRunningCurrentTasks = true;
    private boolean includeTaskScheduleDifferencies = true;

    TestBuilder(final String name, final TestFailureNotifierDecorator failureNotifier,
            final SummaryBuilderFactory summaryBuilderFactory, final FailedInvocationsFactory failedInvocationsFactory,
            final LatencyFactory latencyFactory,
            final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder,
            final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory,
            final TaskScheduleDifferences taskScheduleDifferences,
            final SchedulingServiceFactory schedulingServiceFactory,
            final CategorySpecificReporterFactory categorySpecificReporterFactory,
            final LatencyProviderFactory latencyProviderFactory,
            final TestRuntimeReporterFactory testRuntimeReporterFactory,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider,
            final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider, final SummaryConsumer summaryConsumer) {
        this.name = name;
        this.failureNotifier = failureNotifier;
        this.failedInvocationsFactory = failedInvocationsFactory;
        this.summaryBuilderFactory = summaryBuilderFactory;
        this.latencyFactory = latencyFactory;
        this.allowedExceptionOccurredMessageBuilder = allowedExceptionOccurredMessageBuilder;
        this.schedulingServiceFactory = schedulingServiceFactory;
        this.testRuntimeReporterFactory = testRuntimeReporterFactory;
        this.summaryConsumer = summaryConsumer;
        this.latencyProvider = latencyProviderFactory.newInstance();
        this.timerScheduler = new TimerScheduler();
        this.activeThreads = new ActiveThreads();
        this.scheduledTasks = new ScheduledTasks();
        this.categorySpecificLatencies = new CategorySpecificLatencies(categorySpecificReporterFactory, this);
        this.taskScheduleDifferences = taskScheduleDifferences;
        this.storageForThreadsRunningCurrentTasks = StorageForThreadsRunningCurrentTasks
                .newStorage(lineChartAdapterProvider);
        this.customSummaryAppenders = new ArrayList<>();
        this.runningTasks = LatencyVsConcurrentTasks.instance(scatterPlotAdapterProvider);
        this.allowedExceptions = new AllowedExceptions();
        this.allowedErrors = new AllowedErrors();
        this.adjustedFieldBuilderFactory = adjustedFieldBuilderFactory;
        this.failedInvocations = this.failedInvocationsFactory.newInstance();
        this.lastSecondStats = new LastSecondStatistics(latencyProviderFactory);
        this.lastSecondFailures = new LastSecondFailures(this.failedInvocationsFactory);
        this.lastSecondFailureGraphWriter = new LastSecondFailuresGraphWriter(this.lastSecondFailures,
                this.latencyProvider, lineChartAdapterProvider);
        this.customLatencyReporters = new ArrayList<>();
        this.customLatencyReporters.add(lastSecondStatistics());
        this.customFailureReporters = new ArrayList<>();
        this.customFailureReporters.add(lastSecondFailures());
        this.lastSecondThroughput = new LastSecondThroughput(lineChartAdapterProvider);
        this.categorySpecificLatenciesConfigurator = new CategorySpecificLatenciesConfigurator(
                this.categorySpecificLatencies, categorySpecificReporterFactory, this);
        log().info("TestBuilder for '{}' created.", id());
    }

    @Override
    public TestRuntimeReporter newReporter(final LatencyProvider latencyProvider, final int threads) {
        return newInvocationReporterWithDefaults(latencyProvider, newFailedInvocations(), includeInvocationGraph(),
                buildSetup(0, threads, 0).build());
    }

    private TestRuntimeReporter newInvocationReporterWithDefaults(final LatencyProvider latencyProvider,
            final int threads, final FailedInvocations newFailedInvocations, final boolean includeInvocationGraph) {
        final PerformanceTestSetup setup = setupWithAppenders(0, threads, 0);
        return newInvocationReporterWithDefaults(latencyProvider, newFailedInvocations, includeInvocationGraph, setup);
    }

    private TestRuntimeReporter newInvocationReporterWithDefaults(final LatencyProvider latencyProvider,
            final FailedInvocations newFailedInvocations, final boolean includeInvocationGraph,
            final PerformanceTestSetup setup) {
        return testRuntimeReporterFactory().newRuntimeReporter(latencyProvider, includeInvocationGraph, setup,
                newFailedInvocations);
    }

    private TestRuntimeReporterFactory testRuntimeReporterFactory() {
        return this.testRuntimeReporterFactory;
    }

    /**
     * Calling this will set test to draw no invocation graphs, also no category
     * specific invocation graphs will be drawn.
     */
    public TestBuilder noInvocationGraph() {
        this.includeInvocationGraph = false;
        return this;
    }

    private boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    @Override
    public String id() {
        return this.name;
    }

    private FailedInvocations newFailedInvocations() {
        return failedInvocationsFactory().newInstance();
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    /**
     * @param workers
     *            The number of workers for running test tasks, default value is the
     *            amount of agents;
     */
    public TestBuilder workers(final int workers) {
        this.workers = workers;
        return this;
    }

    public TestBuilder workerWaitTime(final int waitTimeInMillis) {
        this.workerWaitTime = waitTimeInMillis;
        return this;
    }

    public TestBuilder intermediateStatisticsInterval(final int intermediateStatisticsInterval) {
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
        final AdjustedFieldBuilder fieldBuilder = this.adjustedFieldBuilderFactory.newInstance();
        this.intermediateSummaryLogger = summaryBuilderFactory().intermediateSummaryBuilder(latencyProvider(),
                activeThreads(), scheduledTasks(), failedInvocations(),
                newLastSecondStatsProvider(lastSecondStatistics(), fieldBuilder), lastSecondFailures());
        log().debug("Intermediate summary builder created.");
    }

    private LastSecondFailures lastSecondFailures() {
        return this.lastSecondFailures;
    }

    private LastSecondStatistics lastSecondStatistics() {
        return this.lastSecondStats;
    }

    private FailedInvocations failedInvocations() {
        return this.failedInvocations;
    }

    private LastSecondIntermediateStatisticsProvider newLastSecondStatsProvider(
            final LastSecondStatistics statisticsProvider, final AdjustedFieldBuilder fieldBuilder) {
        return new LastSecondIntermediateStatisticsProvider(fieldBuilder, statisticsProvider, lastSecondThroughput());
    }

    private LastSecondThroughput lastSecondThroughput() {
        return this.lastSecondThroughput;
    }

    private SummaryBuilderFactory summaryBuilderFactory() {
        return this.summaryBuilderFactory;
    }

    private void createOverallReporter() {
        this.overallReporter = newInvocationReporterWithDefaults(latencyProvider(), workerThreads(),
                failedInvocations(), includeInvocationGraph());
        log().debug("Overall reporter created.");
    }

    private int workerThreads() {
        return workers() > 0 ? workers() : agents().size();
    }

    private void newSchedulingService() {
        this.schedulingService = schedulingServiceFactory().newSchedulingService(this, workerThreads(),
                scheduledTasks());
        log().debug("Scheduling service created");
    }

    private SchedulingServiceFactory schedulingServiceFactory() {
        return this.schedulingServiceFactory;
    }

    private int workers() {
        return this.workers;
    }

    private void addAgents(final Collection<TestAgent> agents) {
        agents().clear();
        agents().addAll(agents);
        log().debug("Added '{}' agents.", agents().size());
    }

    /**
     * Define which categories will have latency graphs. Notice that if
     * noInvocationGraph() has been called, this has eventually no effect.
     */
    public TestBuilder latencyGraphFor(final TestTaskCategory... categories) {
        categorySpecificLatenciesConfigurator().latencyGraphFor(categories);
        return this;
    }

    public TestBuilder latencyGraphForAll() {
        categorySpecificLatenciesConfigurator().latencyGraphForAll();
        return this;
    }

    private CategorySpecificLatenciesConfigurator categorySpecificLatenciesConfigurator() {
        return this.categorySpecificLatenciesConfigurator;
    }

    @Override
    public void start() {
        log().info("Starting agent based test: {}", id());
        warmUp();
        try {
            run();
        } finally {
            stopRunning();
            log().info("Finished agent based test: {}", id());
        }
    }

    private void run() {
        log().info("Running {} until no scheduled tasks...", id());
        schedulingService().run();
        log().info("No more scheduled tasks for {}.", id());
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
            throw new PerftenceRuntimeException(e);
        }
    }

    private void stopRunning() {
        log().info("Finished all tasks for {}.", id());
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
        log().info("Creating category specific summaries...");
        categorySpecificLatencies().summaryTime();
        final AggregateSummary aggregateSummary = new AggregateSummary();
        categorySpecificLatencies().summaries(new Function<LatencyProvider, TestSummaryLogger>() {

            @Override
            public TestSummaryLogger apply(LatencyProvider latencyProvider) {
                return TestBuilder.this.summaryBuilderFactory.overallSummaryBuilder(latencyProvider);
            }
        }, this.summaryConsumer, aggregateSummary);
        aggregateSummary.publish();
        log().info("Category specific summaries done.");
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
        timerScheduler().schedule(intermediateStatisticsTimer(intermediateStatisticsInterval()));
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
                        intermediateSummaryLogger().printSummary(TestBuilder.this.id(), null);
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
        newSummary(id() + "-overall-statistics", duration, sampleCount, latencyProvider().startTime());
    }

    private void printOverallSummary() {
        String id = id();
        String summaryId = id + ".agent.summary";
        overAllSummaryBuilder().printSummary(id,
                summary -> this.summaryConsumer.consumeSummary(summaryId, SummaryToCsv.convertToCsv(summary)));
    }

    private void newSummary(final String name, final long duration, final long sampleCount, long startTime) {
        overallReporter().summary(name, duration, sampleCount, startTime);
    }

    private TestSummaryLogger overAllSummaryBuilder() {
        return summaryBuilderFactory().overallSummaryBuilder(latencyProvider());
    }

    private PerformanceTestSetup setupWithAppenders(final int duration, final int threads, final int sampleCount) {
        final PerformanceTestSetupBuilder testSetupBuilder = buildSetup(duration, threads, sampleCount);
        if (includeThreadsRunningCurrentTasks()) {
            testSetupBuilder.summaryAppender(threadsRunningCurrentTasks().summaryAppender());
        }

        if (includeTaskScheduleDifferencies()) {
            testSetupBuilder.summaryAppender(taskScheduleDifferencies().summaryAppender());
        }

        for (final SummaryAppender appender : customSummaryAppenders()) {
            testSetupBuilder.summaryAppender(appender);
        }

        if (includeThreadsRunningCurrentTasks()) {
            testSetupBuilder.graphWriter(threadsRunningCurrentTasks().graphWriterFor(id()));
        }

        if (includeTaskScheduleDifferencies()) {
            testSetupBuilder.graphWriter(taskScheduleDifferencies().graphWriterFor(id()));
        }
        testSetupBuilder.graphWriter(runningTasks().graphWriterFor(id()));
        testSetupBuilder.graphWriter(lastSecondThroughput().graphWriterFor(id()));
        testSetupBuilder.graphWriter(lastSecondFailureGraphWriter().graphWriterFor(id()));
        return testSetupBuilder.build();
    }

    private LastSecondFailuresGraphWriter lastSecondFailureGraphWriter() {
        return this.lastSecondFailureGraphWriter;
    }

    private boolean includeTaskScheduleDifferencies() {
        return this.includeTaskScheduleDifferencies;
    }

    private PerformanceTestSetupBuilder buildSetup(final int duration, final int threads, final int sampleCount) {
        return PerformanceTestSetupPojo.builder().duration(duration).threads(threads).invocations(sampleCount)
                .invocationRange(invocationRange()).throughputRange(throughputRange());
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
                    return "TimeSpecification [time()=" + time() + ", timeUnit()=" + timeUnit() + "]";
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
                    if (includeTaskScheduleDifferencies()) {
                        reportWhenDifference(callStart - timeItWasScheduled);
                    }
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
        overallReporter().throughput(latencyProvider().currentDuration(), latencyProvider().currentThroughput());
    }

    private void reportLatencyForRunningTasks(final int activeTasks, final int latency) {
        runningTasks().report(activeTasks, latency);
    }

    private void reportWhenDifference(final long difference) {
        if (includeTaskScheduleDifferencies()) {
            taskScheduleDifferencies().report(difference);
        }
    }

    private void storeThreadsRunningCurrentTasks(final int threads) {
        if (includeThreadsRunningCurrentTasks()) {
            threadsRunningCurrentTasks().store(latencyProvider().currentDuration(), threads);
        }
    }

    private synchronized void handleFailure(final TestTask task, final Throwable cause) {
        final boolean allowed = isAllowed(cause);
        if (!allowed) {
            log().error(taskFailedMessage(task), cause);
        }
        schedulingService().handleFailure(task, cause);
        categorySpecificLatencies().reportFailure(task.category(), cause);
        overallReporter().invocationFailed(cause);
        for (final CustomFailureReporter custom : customFailureReporters()) {
            custom.more(cause);
        }
        if (!allowed) {
            failureNotifier().testFailed(cause);
            finishedWithError(task, cause);
        } else {
            log().warn(allowedExceptionOrErrorOccurredMessage(cause));
        }
    }

    private List<CustomFailureReporter> customFailureReporters() {
        return this.customFailureReporters;
    }

    private String allowedExceptionOrErrorOccurredMessage(final Throwable cause) {
        if (cause instanceof Exception)
            return allowedExceptionOccurredMessageBuilder().allowedExceptionOccurredMessage((Exception) cause, id());
        else if (cause instanceof Error) {
            return allowedExceptionOccurredMessageBuilder().allowedErrorOccurredMessage((Error) cause, id());
        } else {
            return "Failure was either Exception nor Error, cause " + cause.getClass().getSimpleName();
        }
    }

    private AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder() {
        return this.allowedExceptionOccurredMessageBuilder;
    }

    private boolean isAllowed(final Throwable cause) {
        if (cause instanceof Exception)
            return allowedExceptions().isAllowed((Exception) cause);
        if (cause instanceof Error)
            return allowedErrors().isAllowed((Error) cause);
        return false;
    }

    private AllowedErrors allowedErrors() {
        return this.allowedErrors;
    }

    private static void finishedWithError(final TestTask task, final Throwable cause) {
        log().error("Finished task {} with error {} !", task, cause);
    }

    private static String taskFailedMessage(final TestTask task) {
        return new StringBuilder("Task ").append(task.toString()).append(" failed ").toString();
    }

    private synchronized void markDone(final TestTask task) {
        schedulingService().markDone(task);
    }

    private synchronized void addSample(final int latency, final TestTaskCategory category) {
        latencyProvider().addSample(latency);
        overallReporter().latency(latency);
        for (final CustomInvocationReporter reporter : customLatencyReporters()) {
            reporter.latency(latency);
        }
        categorySpecificLatencies().reportLatencyFor(latency, category);
    }

    private List<CustomInvocationReporter> customLatencyReporters() {
        return this.customLatencyReporters;
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

    private TestRuntimeReporter overallReporter() {
        return this.overallReporter;
    }

    private LatencyProvider latencyProvider() {
        return this.latencyProvider;
    }

    private TestFailureNotifierDecorator failureNotifier() {
        return this.failureNotifier;
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

    /**
     * Assign summary appender for the test macchina.
     */
    public TestBuilder summaryAppender(final SummaryAppender... appenders) {
        for (final SummaryAppender appender : appenders) {
            addAppender(appender);
        }
        return this;
    }

    private void addAppender(final SummaryAppender appender) {
        customSummaryAppenders().add(appender);
    }

    /**
     * Setting invocation range for the graph. Notice that if noInvocationGraph()
     * has been called, this has eventually no effect.
     */
    public TestBuilder invocationRange(final int invocationRange) {
        this.invocationRange = invocationRange;
        return this;
    }

    /**
     * Setting throughput range for the graph.
     */
    public TestBuilder throughputRange(final int throughputRange) {
        this.throughputRange = throughputRange;
        return this;
    }

    /**
     * Set the allowed exceptions for the test.
     */
    public TestBuilder allow(final Class<? extends Exception> allowed) {
        allowedExceptions().allow(allowed);
        return this;
    }

    /**
     * Set the allowed errors for the test (e.g. AssertionError)
     */
    public TestBuilder allowError(final Class<? extends Error> allowed) {
        allowedErrors().allow(allowed);
        return this;
    }

    private AllowedExceptions allowedExceptions() {
        return this.allowedExceptions;
    }

    /**
     * Turning off summary and graph for ThreadsRunningCurrentTasks
     */
    public TestBuilder noThreadsRunningCurrentTasks() {
        this.includeThreadsRunningCurrentTasks = false;
        return this;
    }

    private boolean includeThreadsRunningCurrentTasks() {
        return this.includeThreadsRunningCurrentTasks;
    }

    /**
     * Turning off summary and graph for TaskScheduleDifferencies
     */
    public TestBuilder noTaskScheduleDifferencies() {
        this.includeTaskScheduleDifferencies = false;
        return this;
    }

    class AggregateSummary implements BiConsumer<TestTaskCategory, CsvSummary> {
        private final Map<String, List<CategorySummary>> summaries = new HashMap<>();

        AggregateSummary() {
        }

        @Override
        public void accept(TestTaskCategory category, CsvSummary summary) {
            String key = summary.columnRow();
            if (!this.summaries.containsKey(key)) {
                this.summaries.put(key, new ArrayList<>());
            }
            this.summaries.get(summary.columnRow()).add(new CategorySummary(category, summary));
        }

        class CategorySummary {
            private final TestTaskCategory category;
            private final CsvSummary summary;

            private CategorySummary(TestTaskCategory category, CsvSummary summary) {
                this.category = category;
                this.summary = summary;
            }
        }

        public void publish() {
            int i = 1;
            for (Entry<String, List<CategorySummary>> entry : this.summaries.entrySet()) {
                List<CategorySummary> categorySummaries = entry.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append("category").append(",").append(entry.getKey()).append("\n");
                for (CategorySummary categorySummary : categorySummaries) {
                    String row = categorySummary.category.name() + "," + categorySummary.summary.valueRow();
                    sb.append(row).append("\n");
                }
                TestBuilder.this.summaryConsumer.consumeSummary(id() + ".aggregate." + i + ".csv", sb.toString());
                i++;
            }
        }
    }
}
