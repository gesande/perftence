package net.sf.perftence.fluent;

import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.RuntimeStatisticsProvider;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummaryBuilder;
import net.sf.perftence.reporting.summary.TestSummaryLogger;
import net.sf.perftence.reporting.summary.TestSummaryLoggerFactory;

public final class SummaryBuilderFactory {

    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final SummaryFieldFactory summaryFieldFactory;

    public SummaryBuilderFactory(final SummaryFieldFactory summaryFieldFactory,
            final TestSummaryLoggerFactory testSummaryLoggerFactory) {
        this.summaryFieldFactory = summaryFieldFactory;
        this.testSummaryLoggerFactory = testSummaryLoggerFactory;
    }

    public TestSummaryLogger overAllSummaryBuilder(
            final PerformanceTestSetup setUp, final StatisticsProvider provider) {
        return newTestSummaryLogger(new OverallSummaryBuilder(setUp, provider,
                summaryFieldFactory()));
    }

    public TestSummaryLogger intermediateSummaryBuilder(
            final PerformanceTestSetup setUp,
            final RuntimeStatisticsProvider provider,
            final CustomIntermediateSummaryProvider... providers) {
        return newTestSummaryLogger(new IntermediateSummaryBuilder(setUp,
                provider, summaryFieldFactory())
                .customSummaryProviders(providers));
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private TestSummaryLogger newTestSummaryLogger(
            final TestSummaryBuilder builder) {
        return testSummaryLoggerFactory().newSummaryLogger(builder);
    }

    private TestSummaryLoggerFactory testSummaryLoggerFactory() {
        return this.testSummaryLoggerFactory;
    }

}
