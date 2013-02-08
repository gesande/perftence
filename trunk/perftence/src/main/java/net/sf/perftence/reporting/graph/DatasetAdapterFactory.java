package net.sf.perftence.reporting.graph;

import net.sf.perftence.reporting.graph.jfreechart.BarChartGraphData;
import net.sf.perftence.reporting.graph.jfreechart.LineChartGraphData;
import net.sf.perftence.reporting.graph.jfreechart.ScatterPlotGraphData;

public interface DatasetAdapterFactory {
    DatasetAdapter<LineChartGraphData> forLineChart(final String title);

    DatasetAdapter<BarChartGraphData> forBarChart(final String legendTitle);

    DatasetAdapter<ScatterPlotGraphData> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
