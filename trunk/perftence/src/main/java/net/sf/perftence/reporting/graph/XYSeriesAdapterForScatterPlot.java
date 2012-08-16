package net.sf.perftence.reporting.graph;

import java.awt.Paint;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

class XYSeriesAdapterForScatterPlot implements
        DatasetAdapter<ScatterPlotGraphData> {

    private final XYSeries series;
    private final String yAxisTitle;

    XYSeriesAdapterForScatterPlot(final String legendTitle,
            final String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
        this.series = new XYSeries(legendTitle);
    }

    private XYSeries series() {
        return this.series;
    }

    @Override
    public void add(final Number x, final Number y) {
        series().add(x, y, false);
    }

    @Override
    public ScatterPlotGraphData graphData(final Paint paint, final double range) {
        final XYSeriesCollection result = new XYSeriesCollection();
        result.addSeries(series());
        return new ScatterPlotGraphData(this.yAxisTitle, result);
    }
}