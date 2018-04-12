package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.BarChartAdapterProvider;
import org.fluentjava.perftence.graph.DatasetAdapter;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;

public interface DatasetAdapterFactory extends LineChartAdapterProvider<LineChartGraphData, Paint>,
        BarChartAdapterProvider<BarChartGraphData, Paint>, ScatterPlotAdapterProvider<ScatterPlotGraphData, Paint> {
    @Override
    DatasetAdapter<LineChartGraphData, Paint> forLineChart(final String title);

    @Override
    DatasetAdapter<BarChartGraphData, Paint> forBarChart(final String legendTitle);

    @Override
    DatasetAdapter<ScatterPlotGraphData, Paint> forScatterPlot(final String legendTitle, final String yAxisTitle);

}
