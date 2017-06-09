package net.sf.perftence.fluent;

import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.summary.AbstractSummaryBuilder;
import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummary;
import net.sf.perftence.setup.PerformanceTestSetup;

final class OverallSummaryBuilder extends AbstractSummaryBuilder {

    private final StatisticsProvider provider;
    private final PerformanceTestSetup setUp;
    private final SummaryFieldFactory summaryFieldFactory;
    private final EstimatedInvocations estimatedInvocations;

    OverallSummaryBuilder(final PerformanceTestSetup setUp, final StatisticsProvider provider,
            final SummaryFieldFactory summaryFieldFactory, final EstimatedInvocations estimatedInvocations) {
        super();
        this.setUp = setUp;
        this.provider = provider;
        this.summaryFieldFactory = summaryFieldFactory;
        this.estimatedInvocations = estimatedInvocations;
    }

    @Override
    public void fields(final TestSummary summary) {
        final long duration = provider().duration();
        final double throughput = provider().throughput();
        final long sampleCount = provider().sampleCount();
        if (setUp().invocations() > 0) {
            summary.field(
                    summaryFieldFactory().samples().samplesSoFar(sampleCount).samplesTotal(setUp().invocations()));
        } else {
            summary.field(summaryFieldFactory().estimatedSamples().samplesSoFar(sampleCount).estimatedSamples(
                    estimatedInvocations().calculate(throughput, setUp().duration(), provider().sampleCount())));
        }
        summary.field(max(provider().maxLatency()));
        summary.field(average(provider().averageLatency()).asFormatted());
        summary.field(median(provider().median()));
        summary.field(percentile95(provider().percentileLatency(95)));
        summary.field(throughput(throughput).asFormatted());
        summary.field(executionTime(duration));
        summary.field(threads(setUp().threads()));
    }

    @Override
    public boolean hasSamples() {
        return provider().hasSamples();
    }

    private SummaryField<Long> executionTime(long duration) {
        return summaryFieldFactory().executionTime().value(duration).build();
    }

    private BuildableSummaryField<Double> throughput(double value) {
        return summaryFieldFactory().throughput().value(value);
    }

    private SummaryField<Long> percentile95(final long value) {
        return summaryFieldFactory().percentile95().value(value).build();
    }

    private SummaryField<Long> median(final long value) {
        return summaryFieldFactory().median().value(value).build();
    }

    private BuildableSummaryField<Double> average(final double averageLatency) {
        return summaryFieldFactory().average().value(averageLatency);
    }

    private SummaryField<Long> max(final long maxLatency) {
        return summaryFieldFactory().max().value(maxLatency).build();
    }

    private SummaryField<Integer> threads(final int value) {
        return summaryFieldFactory().threads().value(value).build();
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private PerformanceTestSetup setUp() {
        return this.setUp;
    }

    private StatisticsProvider provider() {
        return this.provider;
    }

    private EstimatedInvocations estimatedInvocations() {
        return this.estimatedInvocations;
    }

}