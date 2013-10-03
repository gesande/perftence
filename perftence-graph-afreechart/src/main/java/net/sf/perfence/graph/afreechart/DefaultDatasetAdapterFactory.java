package net.sf.perfence.graph.afreechart;

import net.sf.perftence.graph.DatasetAdapter;

import org.afree.graphics.PaintType;

public final class DefaultDatasetAdapterFactory implements
        DatasetAdapterFactory {

    @Override
    public DatasetAdapter<LineChartGraphData, PaintType> forLineChart(
            final String title) {
        return new XYSeriesAdapter(title);
    }

    @Override
    public DatasetAdapter<BarChartGraphData, PaintType> forBarChart(
            final String legendTitle) {
        return new CategoryDatasetAdapter(legendTitle);
    }

    @Override
    public DatasetAdapter<ScatterPlotGraphData, PaintType> forScatterPlot(
            final String legendTitle, final String yAxisTitle) {
        return new XYSeriesAdapterForScatterPlot(legendTitle, yAxisTitle);
    }
}
