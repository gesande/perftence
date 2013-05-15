package net.sf.perftence.common;

import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;

public class StatisticsSummaryProviderUsingStatisticsProviderStatistics
        implements StatisticsSummaryProvider<HtmlSummary> {

    private final StatisticsProvider statistics;

    public StatisticsSummaryProviderUsingStatisticsProviderStatistics(
            final StatisticsProvider statistics) {
        this.statistics = statistics;
    }

    @Override
    public void provideSummary(final HtmlSummary summary) {
        summary.throughput(statistics().throughput()).endOfLine();
        summary.minResponseTime(statistics().minLatency(), " ms").endOfLine();
        summary.maxResponseTime(statistics().maxLatency(), " ms").endOfLine();
        summary.averageResponseTime(statistics().averageLatency(), " ms")
                .endOfLine();
        summary.median(statistics().median(), " ms").endOfLine();
        summary.percentileHeader();
        summary.percentile90(statistics().percentileLatency(90));
        summary.percentile95(statistics().percentileLatency(95));
        summary.percentile96(statistics().percentileLatency(96));
        summary.percentile97(statistics().percentileLatency(97));
        summary.percentile98(statistics().percentileLatency(98));
        summary.percentile99(statistics().percentileLatency(99));
    }

    @Override
    public StatisticsSummaryProviderUsingStatisticsProviderStatistics elapsedTime(
            final long elapsedTime) {
        return this;
    }

    @Override
    public StatisticsSummaryProviderUsingStatisticsProviderStatistics invocations(
            final long invocationCount) {
        return this;
    }

    private StatisticsProvider statistics() {
        return this.statistics;
    }
}
