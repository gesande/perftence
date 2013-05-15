package net.sf.perftence.common;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.graph.DatasetAdapterFactory;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.graph.jfreechart.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.ReportingOptionsFactory;
import net.sf.perftence.reporting.TestReport;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DefaultTestRuntimeReporterFactory implements
        TestRuntimeReporterFactory {

    private final TestReport testReport;
    private final DatasetAdapterFactory datasetAdapterFactory;
    private final ThroughputStorageFactory throughputStorageFactory;
    private final ImageFactory imageFactory;

    public DefaultTestRuntimeReporterFactory() {
        this.testReport = HtmlTestReport.withDefaultReportPath();
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.throughputStorageFactory = new ThroughputStorageFactory(
                this.datasetAdapterFactory);
        this.imageFactory = new ImageFactoryUsingJFreeChart(this.testReport);
    }

    @Override
    public TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations) {
        return newRuntimeReporter(latencyProvider, includeInvocationGraph,
                setup, failedInvocations,
                resolveInvocationStorage(includeInvocationGraph, setup),
                this.throughputStorageFactory.forRange(setup.throughputRange()));
    }

    private InvocationStorage resolveInvocationStorage(
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup) {
        return includeInvocationGraph ? defaultInvocationStorage(setup)
                : invocationStorageWithNoSamples();
    }

    private InvocationStorage defaultInvocationStorage(
            final PerformanceTestSetup setup) {
        return DefaultInvocationStorage.newDefaultStorage(setup.invocations(),
                ReportingOptionsFactory.latencyOptionsWithStatistics(setup
                        .invocationRange()), this.datasetAdapterFactory);
    }

    private InvocationStorage invocationStorageWithNoSamples() {
        return DefaultInvocationStorage
                .invocationStorageWithNoSamples(this.datasetAdapterFactory);
    }

    @Override
    public TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final ThroughputStorage throughputStorage) {
        return new DefaultTestRuntimeReporter(invocationStorage,
                throughputStorage, this.imageFactory, setup.threads(),
                setup.duration(), FrequencyStorageFactory.newFrequencyStorage(
                        latencyProvider, this.datasetAdapterFactory),
                setup.summaryAppenders(), includeInvocationGraph,
                setup.graphWriters(), statisticsSummaryProvider(
                        latencyProvider, includeInvocationGraph,
                        invocationStorage), failedInvocations, this.testReport);
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

}
