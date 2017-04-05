package net.sf.perftence.common;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.FailedInvocations;
import net.sf.perftence.reporting.summary.HtmlSummary;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DefaultTestRuntimeReporterFactory
		implements TestRuntimeReporterFactory {

	private final ReporterFactoryDependencies deps;

	public DefaultTestRuntimeReporterFactory(
			final ReporterFactoryDependencies deps) {
		this.deps = deps;
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
				dependencies().throughputStorageFactory()
						.forRange(setup.throughputRange()));
	}

	private ReporterFactoryDependencies dependencies() {
		return this.deps;
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
				ReportingOptionsFactory
						.latencyOptionsWithStatistics(setup.invocationRange()),
				dependencies().lineChartAdapterProvider());
	}

	private InvocationStorage invocationStorageWithNoSamples() {
		return DefaultInvocationStorage.invocationStorageWithNoSamples(
				dependencies().lineChartAdapterProvider());
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
				throughputStorage, dependencies().imageFactory(),
				setup.threads(), setup.duration(),
				FrequencyStorageFactory.newFrequencyStorage(latencyProvider,
						dependencies().lineChartAdapterProvider()),
				setup.summaryAppenders(), includeInvocationGraph,
				setup.graphWriters(),
				statisticsSummaryProvider(latencyProvider,
						includeInvocationGraph, invocationStorage),
				failedInvocations, dependencies().testReport());
	}

	private static StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider(
			final StatisticsProvider statisticsProvider,
			final boolean includeInvocationGraph,
			final InvocationStorage invocationStorage) {
		return includeInvocationGraph
				? new StatisticsSummaryProviderUsingDefaultInvocationStorageStatistics(
						invocationStorage)
				: new StatisticsSummaryProviderUsingStatisticsProviderStatistics(
						statisticsProvider);
	}

}
