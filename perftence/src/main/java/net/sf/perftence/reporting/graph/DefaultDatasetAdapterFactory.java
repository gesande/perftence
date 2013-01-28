package net.sf.perftence.reporting.graph;

public final class DefaultDatasetAdapterFactory implements
        DatasetAdapterFactory {

    @Override
    public DatasetAdapter<LineChartGraphData> forLineChart(final String title) {
        return new XYSeriesAdapter(title);
    }

    @Override
    public DatasetAdapter<BarChartGraphData> forBarChart(
            final String legendTitle) {
        return new CategoryDatasetAdapter(legendTitle);
    }

    @Override
    public DatasetAdapter<ScatterPlotGraphData> forScatterPlot(
            final String legendTitle, final String yAxisTitle) {
        return new XYSeriesAdapterForScatterPlot(legendTitle, yAxisTitle);
    }
}
