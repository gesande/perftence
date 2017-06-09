package net.sf.perftence.graph.jfreechart;

import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.ReporterFactoryDependencies;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.common.ThroughputStorageFactory;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.graph.ScatterPlotAdapterProvider;
import net.sf.perftence.reporting.TestReport;

public final class TestRuntimeReporterFactoryUsingJFreeChart implements ReporterFactoryDependencies {

    private TestReport testReport;
    private DefaultDatasetAdapterFactory datasetAdapterFactory;
    private ThroughputStorageFactory throughputStorageFactory;
    private ImageFactoryUsingJFreeChart imageFactory;

    public TestRuntimeReporterFactoryUsingJFreeChart(TestReport testReport) {
        this.testReport = testReport;
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.throughputStorageFactory = new ThroughputStorageFactory(this.datasetAdapterFactory);
        this.imageFactory = new ImageFactoryUsingJFreeChart(
                new JFreeChartWriter(this.testReport.reportRootDirectory()));
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

    public static TestRuntimeReporterFactory reporterFactory(TestReport testReport) {
        return new DefaultTestRuntimeReporterFactory(new TestRuntimeReporterFactoryUsingJFreeChart(testReport));
    }

    public ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider() {
        return this.datasetAdapterFactory;
    }

}
