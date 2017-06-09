package net.sf.perfence.graph.afreechart;

import org.afree.graphics.PaintType;

import net.sf.perftence.graph.BarChartAdapterProvider;
import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.graph.ScatterPlotAdapterProvider;

public interface DatasetAdapterFactory extends LineChartAdapterProvider<LineChartGraphData, PaintType>,
        BarChartAdapterProvider<BarChartGraphData, PaintType>,
        ScatterPlotAdapterProvider<ScatterPlotGraphData, PaintType> {
    @Override
    DatasetAdapter<LineChartGraphData, PaintType> forLineChart(final String title);

    @Override
    DatasetAdapter<BarChartGraphData, PaintType> forBarChart(final String legendTitle);

    @Override
    DatasetAdapter<ScatterPlotGraphData, PaintType> forScatterPlot(final String legendTitle, final String yAxisTitle);

}
