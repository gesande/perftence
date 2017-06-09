package net.sf.perftence.agents;

import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.summary.AbstractSummaryBuilder;
import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.TestSummary;

final class OverallSummaryBuilder extends AbstractSummaryBuilder {

    private final TestFailureNotifierDecorator failureNotifierAdapter;
    private final StatisticsProvider statistics;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;

    OverallSummaryBuilder(final TestFailureNotifierDecorator failureNotifierAdapter,
            final StatisticsProvider latencyCounter, final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory) {
        this.failureNotifierAdapter = failureNotifierAdapter;
        this.statistics = latencyCounter;
        this.summaryFieldFactory = summaryFieldFactory;
    }

    private StatisticsProvider statistics() {
        return this.statistics;
    }

    @Override
    protected void fields(final TestSummary summary) {
        summary.field(finishedTasks(statistics().sampleCount()));
        summary.field(failedTasks(failureNotifier().failedTasks()));
        summary.field(max(statistics().maxLatency()));
        summary.field(average(statistics().averageLatency()).asFormatted());
        summary.field(median(statistics().median()));
        summary.field(percentile95(statistics().percentileLatency(95)));
        summary.field(throughput(statistics().throughput()).asFormatted());
        summary.field(executionTime(statistics().duration()));
    }

    @Override
    public boolean hasSamples() {
        return statistics().hasSamples();
    }

    private SummaryField<?> executionTime(final long duration) {
        return summaryFieldFactory().executionTime(duration);
    }

    private BuildableSummaryField<Double> throughput(final double currentThroughput) {
        return summaryFieldFactory().throughput(currentThroughput);
    }

    private SummaryField<Long> percentile95(long percentileLatency) {
        return summaryFieldFactory().percentile95(percentileLatency);
    }

    private SummaryField<Long> median(final long median) {
        return summaryFieldFactory().median(median);
    }

    private BuildableSummaryField<Double> average(final double averageLatency) {
        return summaryFieldFactory().average(averageLatency);
    }

    private SummaryField<Long> max(final long maxLatency) {
        return summaryFieldFactory().max(maxLatency);
    }

    private SummaryField<Long> failedTasks(final long value) {
        return summaryFieldFactory().failedTasks(value);
    }

    private SummaryField<Long> finishedTasks(final long value) {
        return summaryFieldFactory().finishedTasks(value);
    }

    private SummaryFieldFactoryForAgentBasedTests summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private TestFailureNotifierDecorator failureNotifier() {
        return this.failureNotifierAdapter;
    }

}