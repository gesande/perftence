package net.sf.perftence.api;

import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.HtmlTestReport;
import net.sf.perftence.fluent.FileSummaryConsumer;
import net.sf.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import net.sf.perftence.reporting.TestReport;

public final class DefaultPerftenceApiFactory {

	@SuppressWarnings("static-method")
	public PerftenceApi newPerftenceApi(final TestFailureNotifier notifier) {
		TestReport testReport = HtmlTestReport.withDefaultReportPath();
		final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart(
				testReport);
		final DefaultTestRuntimeReporterFactory testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
				deps);
		final PerftenceApi api = new PerftenceApi(notifier,
				testRuntimeReporterFactory, deps.lineChartAdapterProvider(),
				deps.scatterPlotAdapterProvider(),
				new FileSummaryConsumer(testReport.reportRootDirectory()));
		return api;

	}
}