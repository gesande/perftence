package net.sf.perftence.reporting.graph;

public interface DatasetAdapterFactory {
    DatasetAdapter<LineChartGraphData> forLineChart(final String title);

    DatasetAdapter<BarChartGraphData> forBarChart(final String legendTitle);

    DatasetAdapter<ScatterPlotGraphData> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
