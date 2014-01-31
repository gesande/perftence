package net.sf.perftence.api;

import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;

public final class DefaultPerftenceApiFactory {

	@SuppressWarnings("static-method")
	public PerftenceApi newPerftenceApi(final TestFailureNotifier notifier) {
		final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart();
		final DefaultTestRuntimeReporterFactory testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
				deps);
		final PerftenceApi api = new PerftenceApi(notifier,
				testRuntimeReporterFactory, deps.lineChartAdapterProvider(),
				deps.scatterPlotAdapterProvider());
		return api;

	}
}