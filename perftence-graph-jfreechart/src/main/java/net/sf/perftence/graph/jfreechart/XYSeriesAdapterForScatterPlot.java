package net.sf.perftence.graph.jfreechart;

import java.awt.Paint;

import net.sf.perftence.graph.DatasetAdapter;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

class XYSeriesAdapterForScatterPlot implements
        DatasetAdapter<ScatterPlotGraphData> {

    private final XYSeries series;
    private final String yAxisTitle;
    private final static XYSeriesFactory SERIES_FACTORY = new XYSeriesFactory();

    XYSeriesAdapterForScatterPlot(final String legendTitle,
            final String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
        this.series = SERIES_FACTORY.newXYSeries(legendTitle);
    }

    private XYSeries series() {
        return this.series;
    }

    private String yAxisTitle() {
        return this.yAxisTitle;
    }

    @Override
    public void add(final Number x, final Number y) {
        series().add(x, y, false);
    }

    @Override
    public ScatterPlotGraphData graphData(final Paint paint, final double range) {
        final XYSeriesCollection result = new XYSeriesCollection();
        result.addSeries(series());
        return new ScatterPlotGraphData(yAxisTitle(), result);
    }

}