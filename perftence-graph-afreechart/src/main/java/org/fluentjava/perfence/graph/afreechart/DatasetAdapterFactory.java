package org.fluentjava.perfence.graph.afreechart;

import org.afree.graphics.PaintType;
import org.fluentjava.perftence.graph.BarChartAdapterProvider;
import org.fluentjava.perftence.graph.DatasetAdapter;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;

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
