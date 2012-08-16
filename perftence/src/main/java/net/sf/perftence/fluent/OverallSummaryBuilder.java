package net.sf.perftence.fluent;

import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.summary.AbstractSummaryBuilder;
import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummary;

final class OverallSummaryBuilder extends AbstractSummaryBuilder {

    private final StatisticsProvider provider;
    private final PerformanceTestSetup setUp;
    private final SummaryFieldFactory summaryFieldFactory;

    OverallSummaryBuilder(final PerformanceTestSetup setUp,
            final StatisticsProvider provider,
            final SummaryFieldFactory summaryFieldFactory) {
        this.setUp = setUp;
        this.provider = provider;
        this.summaryFieldFactory = summaryFieldFactory;
    }

    @Override
    public void fields(final TestSummary summary) {
        final long duration = provider().duration();
        final double throughput = provider().throughput();
        final long sampleCount = provider().sampleCount();
        if (setUp().invocations() > 0) {
            summary.field(summaryFieldFactory().samples()
                    .samplesSoFar(sampleCount)
                    .samplesTotal(setUp().invocations()));
        } else {
            summary.field(summaryFieldFactory()
                    .estimatedSamples()
                    .samplesSoFar(sampleCount)
                    .estimatedSamples(
                            EstimatedInvocations.calculate(throughput, setUp()
                                    .duration(), provider().sampleCount())));
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

    private final SummaryField<Long> executionTime(long duration) {
        return summaryFieldFactory().executionTime().value(duration).build();
    }

    private final BuildableSummaryField<Double> throughput(double value) {
        return summaryFieldFactory().throughput().value(value);
    }

    private final SummaryField<Long> percentile95(final long value) {
        return summaryFieldFactory().percentile95().value(value).build();
    }

    private final SummaryField<Long> median(final long value) {
        return summaryFieldFactory().median().value(value).build();
    }

    private final BuildableSummaryField<Double> average(
            final double averageLatency) {
        return summaryFieldFactory().average().value(averageLatency);
    }

    private final SummaryField<Long> max(final long maxLatency) {
        return summaryFieldFactory().max().value(maxLatency).build();
    }

    private final SummaryField<Integer> threads(final int value) {
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

}