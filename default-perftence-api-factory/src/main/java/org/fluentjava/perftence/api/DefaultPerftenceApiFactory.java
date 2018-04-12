package org.fluentjava.perftence.api;

import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.DefaultTestRuntimeReporterFactory;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.reporting.TestReport;

public final class DefaultPerftenceApiFactory {

    @SuppressWarnings("static-method")
    public PerftenceApi newPerftenceApi(final TestFailureNotifier notifier) {
        TestReport testReport = HtmlTestReport.withDefaultReportPath();
        final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart(
                testReport);
        final DefaultTestRuntimeReporterFactory testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
                deps);
        final PerftenceApi api = new PerftenceApi(notifier, testRuntimeReporterFactory, deps.lineChartAdapterProvider(),
                deps.scatterPlotAdapterProvider(), new SummaryToFileWriter(testReport.reportRootDirectory()));
        return api;

    }
}