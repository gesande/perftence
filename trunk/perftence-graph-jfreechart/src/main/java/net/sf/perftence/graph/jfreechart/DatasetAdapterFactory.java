package net.sf.perftence.graph.jfreechart;

import java.awt.Paint;

import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.LineChartAdapterProvider;

public interface DatasetAdapterFactory extends
        LineChartAdapterProvider<LineChartGraphData, Paint>,
        BarChartAdapterProvider<BarChartGraphData, Paint>,
        ScatterPlotAdapterProvider<ScatterPlotGraphData, Paint> {
    @Override
    DatasetAdapter<LineChartGraphData, Paint> forLineChart(final String title);

    @Override
    DatasetAdapter<BarChartGraphData, Paint> forBarChart(
            final String legendTitle);

    @Override
    DatasetAdapter<ScatterPlotGraphData, Paint> forScatterPlot(
            final String legendTitle, final String yAxisTitle);

}
