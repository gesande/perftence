package org.fluentjava.perftence.common;

import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.StatisticsProvider;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;
import org.fluentjava.perftence.reporting.summary.FailedInvocations;
import org.fluentjava.perftence.reporting.summary.HtmlSummary;
import org.fluentjava.perftence.reporting.summary.StatisticsSummaryProvider;
import org.fluentjava.perftence.setup.PerformanceTestSetup;

public final class DefaultTestRuntimeReporterFactory implements TestRuntimeReporterFactory {

    private final ReporterFactoryDependencies deps;

    public DefaultTestRuntimeReporterFactory(final ReporterFactoryDependencies deps) {
        this.deps = deps;
    }

    @Override
    public TestRuntimeReporter newRuntimeReporter(final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph, final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations) {
        return newRuntimeReporter(latencyProvider, includeInvocationGraph, setup, failedInvocations,
                resolveInvocationStorage(includeInvocationGraph, setup),
                dependencies().throughputStorageFactory().forRange(setup.throughputRange()));
    }

    private ReporterFactoryDependencies dependencies() {
        return this.deps;
    }

    private InvocationStorage resolveInvocationStorage(final boolean includeInvocationGraph,
            final PerformanceTestSetup setup) {
        return includeInvocationGraph ? defaultInvocationStorage(setup) : invocationStorageWithNoSamples();
    }

    private InvocationStorage defaultInvocationStorage(final PerformanceTestSetup setup) {
        return DefaultInvocationStorage.newDefaultStorage(setup.invocations(),
                ReportingOptionsFactory.latencyOptionsWithStatistics(setup.invocationRange()),
                dependencies().lineChartAdapterProvider());
    }

    private InvocationStorage invocationStorageWithNoSamples() {
        return DefaultInvocationStorage.invocationStorageWithNoSamples(dependencies().lineChartAdapterProvider());
    }

    @Override
    public TestRuntimeReporter newRuntimeReporter(final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph, final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations, final InvocationStorage invocationStorage,
            final ThroughputStorage throughputStorage) {
        return new DefaultTestRuntimeReporter(invocationStorage, throughputStorage, dependencies().imageFactory(),
                setup.threads(), setup.duration(),
                FrequencyStorageFactory.newFrequencyStorage(latencyProvider, dependencies().lineChartAdapterProvider()),
                setup.summaryAppenders(), includeInvocationGraph, setup.graphWriters(),
                statisticsSummaryProvider(latencyProvider, includeInvocationGraph, invocationStorage),
                failedInvocations, dependencies().testReport());
    }

    private static StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider(
            final StatisticsProvider statisticsProvider, final boolean includeInvocationGraph,
            final InvocationStorage invocationStorage) {
        return includeInvocationGraph
                ? new StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics(invocationStorage)
                : new StatisticsSummaryProviderUsingStatisticsProviderStatistics(statisticsProvider);
    }

}
