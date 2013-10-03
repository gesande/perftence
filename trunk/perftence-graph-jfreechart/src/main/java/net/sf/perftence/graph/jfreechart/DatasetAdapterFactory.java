package net.sf.perftence.graph.jfreechart;

import java.awt.Paint;

import net.sf.perftence.graph.DatasetAdapter;

public interface DatasetAdapterFactory {
    DatasetAdapter<LineChartGraphData, Paint> forLineChart(final String title);

    DatasetAdapter<BarChartGraphData, Paint> forBarChart(
            final String legendTitle);

    DatasetAdapter<ScatterPlotGraphData, Paint> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
