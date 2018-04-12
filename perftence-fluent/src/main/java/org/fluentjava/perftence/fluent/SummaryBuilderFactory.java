package org.fluentjava.perftence.fluent;

import org.fluentjava.perftence.RuntimeStatisticsProvider;
import org.fluentjava.perftence.StatisticsProvider;
import org.fluentjava.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import org.fluentjava.perftence.reporting.summary.SummaryFieldFactory;
import org.fluentjava.perftence.reporting.summary.TestSummaryBuilder;
import org.fluentjava.perftence.reporting.summary.TestSummaryLogger;
import org.fluentjava.perftence.reporting.summary.TestSummaryLoggerFactory;
import org.fluentjava.perftence.setup.PerformanceTestSetup;

final class SummaryBuilderFactory {

    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final SummaryFieldFactory summaryFieldFactory;
    private final EstimatedInvocations estimatedInvocations;

    public SummaryBuilderFactory(final SummaryFieldFactory summaryFieldFactory,
            final TestSummaryLoggerFactory testSummaryLoggerFactory, final EstimatedInvocations estimatedInvocations) {
        this.summaryFieldFactory = summaryFieldFactory;
        this.testSummaryLoggerFactory = testSummaryLoggerFactory;
        this.estimatedInvocations = estimatedInvocations;
    }

    public TestSummaryLogger overAllSummaryBuilder(final PerformanceTestSetup setUp,
            final StatisticsProvider provider) {
        return newTestSummaryLogger(
                new OverallSummaryBuilder(setUp, provider, summaryFieldFactory(), estimatedInvocations()));
    }

    private EstimatedInvocations estimatedInvocations() {
        return this.estimatedInvocations;
    }

    public TestSummaryLogger intermediateSummaryBuilder(final PerformanceTestSetup setUp,
            final RuntimeStatisticsProvider provider, final CustomIntermediateSummaryProvider... providers) {
        return newTestSummaryLogger(
                new IntermediateSummaryBuilder(setUp, provider, summaryFieldFactory(), estimatedInvocations())
                        .customSummaryProviders(providers));
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private TestSummaryLogger newTestSummaryLogger(final TestSummaryBuilder builder) {
        return testSummaryLoggerFactory().newSummaryLogger(builder);
    }

    private TestSummaryLoggerFactory testSummaryLoggerFactory() {
        return this.testSummaryLoggerFactory;
    }

}
