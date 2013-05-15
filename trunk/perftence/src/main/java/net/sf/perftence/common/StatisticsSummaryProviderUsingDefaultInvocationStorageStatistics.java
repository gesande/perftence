package net.sf.perftence.common;

import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;

public final class StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics
        implements StatisticsSummaryProvider<HtmlSummary> {

    private final InvocationStorage storage;
    private long elapsedTime;
    private long invocations;

    public StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics(
            final InvocationStorage storage) {
        this.storage = storage;
    }

    @Override
    public void provideSummary(final HtmlSummary summary) {
        final Statistics stat = storage().statistics();
        summary.throughput(stat.throughput(elapsedTime(), invocationCount()))
                .endOfLine();
        summary.minResponseTime(stat.min(), " ms").endOfLine();
        summary.maxResponseTime(stat.max(), " ms").endOfLine();
        summary.averageResponseTime(stat.mean(), " ms").endOfLine();
        summary.median(stat.median(), " ms").endOfLine();
        summary.standardDeviation(stat.standardDeviation()).endOfLine();
        summary.variance(stat.variance()).endOfLine();
        summary.percentileHeader();
        summary.percentile90(stat.percentile90());
        summary.percentile95(stat.percentile95());
        summary.percentile96(stat.percentile96());
        summary.percentile97(stat.percentile97());
        summary.percentile98(stat.percentile98());
        summary.percentile99(stat.percentile99());
    }

    private long invocationCount() {
        return this.invocations;
    }

    private long elapsedTime() {
        return this.elapsedTime;
    }

    @Override
    public StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics elapsedTime(
            long elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    @Override
    public StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics invocations(
            long invocations) {
        this.invocations = invocations;
        return this;
    }

    private InvocationStorage storage() {
        return this.storage;
    }

}
