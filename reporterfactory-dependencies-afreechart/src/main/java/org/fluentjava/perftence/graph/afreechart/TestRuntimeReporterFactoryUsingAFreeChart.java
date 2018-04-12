package org.fluentjava.perftence.graph.afreechart;

import org.fluentjava.perfence.graph.afreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perfence.graph.afreechart.ImageFactoryUsingAFreeChart;
import org.fluentjava.perftence.common.DefaultTestRuntimeReporterFactory;
import org.fluentjava.perftence.common.ReporterFactoryDependencies;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.common.ThroughputStorageFactory;
import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;
import org.fluentjava.perftence.reporting.TestReport;

public final class TestRuntimeReporterFactoryUsingAFreeChart implements ReporterFactoryDependencies {

    private TestReport testReport;
    private DefaultDatasetAdapterFactory datasetAdapterFactory;
    private ThroughputStorageFactory throughputStorageFactory;
    private ImageFactoryUsingAFreeChart imageFactory;

    public TestRuntimeReporterFactoryUsingAFreeChart(final AChartWriterFactory chartWriterFactory,
            final TestReport testReport) {
        this.testReport = testReport;
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.throughputStorageFactory = new ThroughputStorageFactory(this.datasetAdapterFactory);
        this.imageFactory = new ImageFactoryUsingAFreeChart(new SolidColors(),
                chartWriterFactory.chartWriter(this.testReport.reportRootDirectory()));
    }

    @Override
    public ThroughputStorageFactory throughputStorageFactory() {
        return this.throughputStorageFactory;
    }

    @Override
    public LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.datasetAdapterFactory;
    }

    @Override
    public ImageFactory imageFactory() {
        return this.imageFactory;
    }

    @Override
    public TestReport testReport() {
        return this.testReport;
    }

    public static TestRuntimeReporterFactory reporterFactory(final AChartWriterFactory chartWriterFactory,
            final TestReport testReport) {
        return new DefaultTestRuntimeReporterFactory(
                new TestRuntimeReporterFactoryUsingAFreeChart(chartWriterFactory, testReport));
    }

    public ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider() {
        return this.datasetAdapterFactory;
    }

}
