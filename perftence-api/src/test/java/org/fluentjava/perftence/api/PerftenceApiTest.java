package org.fluentjava.perftence.api;

import static org.junit.Assert.assertNotNull;

import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.DefaultTestRuntimeReporterFactory;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.reporting.TestReport;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryToCsv.CsvSummary;
import org.junit.Test;

public final class PerftenceApiTest implements TestFailureNotifier {

    @Test
    public void fluent() {
        assertNotNull(perftenceApi().test("name"));
    }

    @Test
    public void agent() {
        assertNotNull(perftenceApi().agentBasedTest("name"));
    }

    @Test
    public void requirements() {
        assertNotNull(perftenceApi().requirements());
    }

    @Test
    public void setup() {
        assertNotNull(perftenceApi().setup());
    }

    @Override
    public void testFailed(final Throwable t) {
        throw new RuntimeException(t);
    }

    private PerftenceApi perftenceApi() {
        TestReport testReport = HtmlTestReport.withDefaultReportPath();
        final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart(
                testReport);
        final DefaultTestRuntimeReporterFactory testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
                deps);
        return new PerftenceApi(this, testRuntimeReporterFactory, deps.lineChartAdapterProvider(),
                deps.scatterPlotAdapterProvider(), new SummaryConsumer() {
                    @Override
                    public void consumeSummary(String summaryId, CsvSummary convertToCsv) {
                        // no impl
                    }

                    @Override
                    public void consumeSummary(String summaryId, String summary) {
                        // no impl
                    }

                });
    }

}
