package net.sf.perftence.common;

import java.util.ArrayList;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.TestReport;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.graph.jfreechart.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.reporting.summary.html.HtmlSummary;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DefaultInvocationReporterFactory {

    private final static TestReport TEST_REPORT = new HtmlTestReport();
    private final static DatasetAdapterFactory DATASET_ADAPTER_FACTORY = new DefaultDatasetAdapterFactory();
    private final static ThroughputStorageFactory THROUGHPUT_STORAGE_FACTORY = new ThroughputStorageFactory(
            DATASET_ADAPTER_FACTORY);

    public static TestRuntimeReporter newDefaultInvocationReporter(
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

    private static TestRuntimeReporter newDefaultInvocationReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage) {
        return newDefaultInvocationReporter(latencyProvider,
                includeInvocationGraph, setup, failedInvocations,
                invocationStorage, throughputStorage(setup));
    }

    public static TestRuntimeReporter newDefaultInvocationReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final ThroughputStorage throughputStorage) {
        return newDefaultInvocationReporter(
                latencyProvider,
                includeInvocationGraph,
                setup,
                failedInvocations,
                invocationStorage,
                statisticsSummaryProvider(latencyProvider,
                        includeInvocationGraph, invocationStorage),
                throughputStorage);
    }

    private static TestRuntimeReporter newDefaultInvocationReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider,
            final ThroughputStorage throughputStorage) {
        return newDefaultInvocationReporter(includeInvocationGraph, setup,
                failedInvocations, invocationStorage,
                statisticsSummaryProvider, throughputStorage,
                FrequencyStorageFactory.newFrequencyStorage(latencyProvider,
                        datasetAdapterFactory()));
    }

    private static TestRuntimeReporter newDefaultInvocationReporter(
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider,
            final ThroughputStorage throughputStorage,
            final FrequencyStorage newFrequencyStorage) {
        return new DefaultTestRuntimeReporter(invocationStorage,
                throughputStorage, imageFactory(), setup.threads(),
                setup.duration(), newFrequencyStorage,
                setup.summaryAppenders(), includeInvocationGraph,
                setup.graphWriters(), statisticsSummaryProvider,
                failedInvocations, testReport());
    }

    private static TestReport testReport() {
        return TEST_REPORT;
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
        return throughputStorageFactory().forRange(setup.throughputRange());
    }

    private static ThroughputStorageFactory throughputStorageFactory() {
        return THROUGHPUT_STORAGE_FACTORY;
    }

    private static DatasetAdapterFactory datasetAdapterFactory() {
        return DATASET_ADAPTER_FACTORY;
    }

    private static ImageFactory imageFactory() {
        return new ImageFactoryUsingJFreeChart(testReport());
    }

    private static InvocationStorage defaultInvocationStorage(
            final PerformanceTestSetup setup) {
        return InvocationStorageFactory.newDefaultInvocationStorage(
                setup.invocations(), setup.invocationRange(),
                datasetAdapterFactory());
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
                return ImageData.statistics("no samples", "X-axis title", 100,
                        statistics(),
                        datasetAdapterFactory().forLineChart("legend title"));
            }

        };
    }
}
