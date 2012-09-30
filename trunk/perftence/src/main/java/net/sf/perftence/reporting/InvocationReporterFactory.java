package net.sf.perftence.reporting;

import java.util.ArrayList;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.reporting.summary.StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics;
import net.sf.perftence.reporting.summary.StatisticsSummaryProviderUsingStatisticsProviderStatistics;
import net.sf.perftence.reporting.summary.html.HtmlSummary;

public final class InvocationReporterFactory {

    public static InvocationReporter newDefaultInvocationReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations) {
        final InvocationStorage invocationStorage = includeInvocationGraph ? defaultInvocationStorage(setup)
                : invocationStorageWithNoSamples();
        return newDefaultInvocationReporter(latencyProvider,
                includeInvocationGraph, setup, failedInvocations,
                invocationStorage);
    }

    public static InvocationReporter newDefaultInvocationReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage) {
        final StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider = statisticsSummaryProvider(
                latencyProvider, includeInvocationGraph, invocationStorage);
        return new DefaultInvocationReporter(invocationStorage,
                throughputStorage(setup), imageFactory(), setup.threads(),
                setup.duration(),
                FrequencyStorageFactory.newFrequencyStorage(latencyProvider),
                setup.summaryAppenders(), includeInvocationGraph,
                setup.graphWriters(), statisticsSummaryProvider,
                failedInvocations);
    }

    private static StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider(
            final StatisticsProvider statisticsProvider,
            final boolean includeInvocationGraph,
            final InvocationStorage invocationStorage) {
        return includeInvocationGraph ? new StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics(
                invocationStorage)
                : new StatisticsSummaryProviderUsingStatisticsProviderStatistics(
                        statisticsProvider);
    }

    private static ThroughputStorage throughputStorage(
            final PerformanceTestSetup setup) {
        return new DefaultThroughputStorage(setup.throughputRange());
    }

    private static ImageFactory imageFactory() {
        return new ImageFactoryUsingJFreeChart();
    }

    private static InvocationStorage defaultInvocationStorage(
            final PerformanceTestSetup setup) {
        return InvocationStorageFactory.newDefaultInvocationStorage(
                setup.invocations(), setup.invocationRange());
    }

    private static InvocationStorage invocationStorageWithNoSamples() {
        return new InvocationStorage() {

            @Override
            public void store(final int latency) {
                // left empty intentionally
            }

            @Override
            public Statistics statistics() {
                return Statistics.fromLatencies(new ArrayList<Integer>());
            }

            @Override
            public boolean reportedLatencyBeingBelowOne() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public ImageData imageData() {
                String legendTitle = "legend title";
                return ImageData.statistics("no samples", "X-axis title",
                        legendTitle, 100, statistics(),
                        DatasetAdapterFactory.adapterForLineChart(legendTitle));
            }

        };
    }

}
