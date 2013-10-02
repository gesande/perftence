package net.sf.perfence.graph.afreechart;

import net.sf.perftence.graph.DatasetAdapter;

public interface DatasetAdapterFactory {
    DatasetAdapter<LineChartGraphData> forLineChart(final String title);

    DatasetAdapter<BarChartGraphData> forBarChart(final String legendTitle);

    DatasetAdapter<ScatterPlotGraphData> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
