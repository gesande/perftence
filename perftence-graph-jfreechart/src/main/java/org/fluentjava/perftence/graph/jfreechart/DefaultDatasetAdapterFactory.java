package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.DatasetAdapter;

public final class DefaultDatasetAdapterFactory implements DatasetAdapterFactory {

    @Override
    public DatasetAdapter<LineChartGraphData, Paint> forLineChart(final String title) {
        return new XYSeriesAdapter(title);
    }

    @Override
    public DatasetAdapter<BarChartGraphData, Paint> forBarChart(final String legendTitle) {
        return new CategoryDatasetAdapter(legendTitle);
    }

    @Override
    public DatasetAdapter<ScatterPlotGraphData, Paint> forScatterPlot(final String legendTitle,
            final String yAxisTitle) {
        return new XYSeriesAdapterForScatterPlot(legendTitle, yAxisTitle);
    }
}
