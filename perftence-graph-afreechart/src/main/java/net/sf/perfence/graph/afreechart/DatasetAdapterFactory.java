package net.sf.perfence.graph.afreechart;

import net.sf.perftence.graph.DatasetAdapter;

import org.afree.graphics.PaintType;

public interface DatasetAdapterFactory {
    DatasetAdapter<LineChartGraphData, PaintType> forLineChart(
            final String title);

    DatasetAdapter<BarChartGraphData, PaintType> forBarChart(
            final String legendTitle);

    DatasetAdapter<ScatterPlotGraphData, PaintType> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
