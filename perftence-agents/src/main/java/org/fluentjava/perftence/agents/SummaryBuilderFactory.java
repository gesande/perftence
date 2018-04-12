package org.fluentjava.perftence.agents;

import org.fluentjava.perftence.RuntimeStatisticsProvider;
import org.fluentjava.perftence.StatisticsProvider;
import org.fluentjava.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import org.fluentjava.perftence.reporting.summary.TestSummaryBuilder;
import org.fluentjava.perftence.reporting.summary.TestSummaryLogger;
import org.fluentjava.perftence.reporting.summary.TestSummaryLoggerFactory;

public final class SummaryBuilderFactory {

    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;
    private final TestFailureNotifierDecorator failureNotifierDecorator;

    public SummaryBuilderFactory(final TestSummaryLoggerFactory testSummaryLoggerFactory,
            final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory,
            final TestFailureNotifierDecorator failureNotifierDecorator) {
        this.testSummaryLoggerFactory = testSummaryLoggerFactory;
        this.summaryFieldFactory = summaryFieldFactory;
        this.failureNotifierDecorator = failureNotifierDecorator;
    }

    private TestSummaryLogger newTestSummaryLogger(final TestSummaryBuilder summaryBuilder) {
        return testSummaryLoggerFactory().newSummaryLogger(summaryBuilder);
    }

    public TestSummaryLogger intermediateSummaryBuilder(final RuntimeStatisticsProvider statisticsProvider,
            final ActiveThreads activeThreads, final TaskSchedulingStatisticsProvider scheduledTasks,
            final CustomIntermediateSummaryProvider... customProviders) {
        return newTestSummaryLogger(new IntermediateSummaryBuilder(statisticsProvider, activeThreads, scheduledTasks,
                failureNotifier(), summaryFieldFactory()).customSummaryProviders(customProviders));
    }

    public TestSummaryLogger overallSummaryBuilder(final StatisticsProvider statisticsProvider) {
        return newTestSummaryLogger(
                new OverallSummaryBuilder(failureNotifier(), statisticsProvider, summaryFieldFactory()));
    }

    private TestSummaryLoggerFactory testSummaryLoggerFactory() {
        return this.testSummaryLoggerFactory;
    }

    private TestFailureNotifierDecorator failureNotifier() {
        return this.failureNotifierDecorator;
    }

    private SummaryFieldFactoryForAgentBasedTests summaryFieldFactory() {
        return this.summaryFieldFactory;
    }
}
