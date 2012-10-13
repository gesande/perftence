package net.sf.perftence.fluent;

import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.RuntimeStatisticsProvider;
import net.sf.perftence.reporting.summary.AbstractSummaryBuilder;
import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.CompositeCustomIntermediateSummaryProvider;
import net.sf.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummary;

final class IntermediateSummaryBuilder extends AbstractSummaryBuilder {
    private final RuntimeStatisticsProvider statisticsProvider;
    private final PerformanceTestSetup testSetup;
    private final SummaryFieldFactory summaryFieldFactory;
    private final CompositeCustomIntermediateSummaryProvider customIntermediateSummaryProvider;

    IntermediateSummaryBuilder(final PerformanceTestSetup setUp,
            final RuntimeStatisticsProvider counter,
            final SummaryFieldFactory summaryFieldFactory) {
        super();
        this.testSetup = setUp;
        this.statisticsProvider = counter;
        this.summaryFieldFactory = summaryFieldFactory;
        this.customIntermediateSummaryProvider = new CompositeCustomIntermediateSummaryProvider();
    }

    @Override
    public void fields(final TestSummary summary) {
        final long currentDuration = runtimeStatistics().currentDuration();
        final double currentThroughput = runtimeStatistics()
                .currentThroughput();
        final long sampleCount = runtimeStatistics().sampleCount();
        if (testSetup().invocations() > 0) {
            summary.field(summaryFieldFactory().samples()
                    .samplesSoFar(sampleCount)
                    .samplesTotal(testSetup().invocations()));
        } else {
            summary.field(summaryFieldFactory()
                    .estimatedSamples()
                    .samplesSoFar(sampleCount)
                    .estimatedSamples(
                            EstimatedInvocations.calculate(currentThroughput,
                                    testSetup().duration(), runtimeStatistics()
                                            .sampleCount())));
        }
        summary.field(max(runtimeStatistics().maxLatency()));
        summary.field(average(runtimeStatistics().averageLatency())
                .asFormatted());
        summary.field(median(runtimeStatistics().median()));
        summary.field(percentile95(runtimeStatistics().percentileLatency(95)));
        summary.field(throughput(currentThroughput).asFormatted());
        summary.field(executionTime(currentDuration));
        summary.field(threads(testSetup().threads()));
        if (testSetup().invocations() > 0) {
            summary.field(summaryFieldFactory()
                    .estimatedTimeLeftBasedOnThroughput()
                    .invocationsLeft(testSetup().invocations() - sampleCount)
                    .throughput(currentThroughput));
        } else {
            final long actualTimeLeft = (testSetup().duration() - currentDuration) / 1000;
            summary.field(summaryFieldFactory()
                    .estimatedTimeLeftBasedOnDuration()
                    .actualTimeLeft(actualTimeLeft)
                    .estimatedTimeLeft(Math.max(actualTimeLeft, 1)));
        }
        customIntermediateSummaryProvider().intermediateSummary(summary,
                summaryFieldFactory());
    }

    private CompositeCustomIntermediateSummaryProvider customIntermediateSummaryProvider() {
        return this.customIntermediateSummaryProvider;
    }

    public IntermediateSummaryBuilder customSummaryProviders(
            final CustomIntermediateSummaryProvider... providers) {
        customIntermediateSummaryProvider().customSummaryProviders(providers);
        return this;
    }

    @Override
    public boolean hasSamples() {
        return runtimeStatistics().hasSamples();
    }

    private SummaryField<Long> executionTime(final long duration) {
        return summaryFieldFactory().executionTime().value(duration).build();
    }

    private BuildableSummaryField<Double> throughput(final double value) {
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

    private RuntimeStatisticsProvider runtimeStatistics() {
        return this.statisticsProvider;
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private PerformanceTestSetup testSetup() {
        return this.testSetup;
    }

}