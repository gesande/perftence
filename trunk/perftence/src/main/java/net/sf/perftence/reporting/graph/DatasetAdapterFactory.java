package net.sf.perftence.reporting.graph;

public final class DatasetAdapterFactory {

    private DatasetAdapterFactory() {
    }

    public static DatasetAdapter<LineChartGraphData> adapterForLineChart(
            final String title) {
        return new XYSeriesAdapter(title);
    }

    public static DatasetAdapter<BarChartGraphData> adapterForBarChart(
            final String legendTitle) {
        return new CategoryDatasetAdapter(legendTitle);
    }

    public static DatasetAdapter<ScatterPlotGraphData> adapterForScatterPlot(
            final String legendTitle, String yAxisTitle) {
        return new XYSeriesAdapterForScatterPlot(legendTitle, yAxisTitle);
    }
}
