package net.sf.perftence.common;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.ReportingOptionsFactory;
import net.sf.perftence.reporting.TestReport;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.graph.jfreechart.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.reporting.summary.html.HtmlSummary;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DefaultDependencyFactory {

    private final static TestReport TEST_REPORT = new HtmlTestReport();
    private final static DatasetAdapterFactory DATASET_ADAPTER_FACTORY = new DefaultDatasetAdapterFactory();
    private final static ThroughputStorageFactory THROUGHPUT_STORAGE_FACTORY = new ThroughputStorageFactory(
            DATASET_ADAPTER_FACTORY);

    /**
     * @deprecated use {@link DefaultTestRuntimeReporterFactory} instead
     */
    @Deprecated
    public static TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations) {
        return newRuntimeReporter(latencyProvider, includeInvocationGraph,
                setup, failedInvocations,
                resolveInvocationStorage(includeInvocationGraph, setup),
                throughputStorageFactory().forRange(setup.throughputRange()));
    }

    private static InvocationStorage resolveInvocationStorage(
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup) {
        return includeInvocationGraph ? defaultInvocationStorage(setup)
                : invocationStorageWithNoSamples();
    }

    /**
     * @deprecated use {@link DefaultTestRuntimeReporterFactory} instead
     */
    @Deprecated
    private static TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final ThroughputStorage throughputStorage) {
        return new DefaultTestRuntimeReporter(invocationStorage,
                throughputStorage, imageFactory(), setup.threads(),
                setup.duration(), FrequencyStorageFactory.newFrequencyStorage(
                        latencyProvider, datasetAdapterFactory()),
                setup.summaryAppenders(), includeInvocationGraph,
                setup.graphWriters(), statisticsSummaryProvider(
                        latencyProvider, includeInvocationGraph,
                        invocationStorage), failedInvocations, testReport());
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
        return DefaultInvocationStorage.newDefaultStorage(setup.invocations(),
                ReportingOptionsFactory.latencyOptionsWithStatistics(setup
                        .invocationRange()), datasetAdapterFactory());
    }

    private static InvocationStorage invocationStorageWithNoSamples() {
        return DefaultInvocationStorage
                .invocationStorageWithNoSamples(datasetAdapterFactory());
    }
}
