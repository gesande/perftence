package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.DatasetAdapter;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;

final class XYSeriesAdapter implements DatasetAdapter<LineChartGraphData, Paint> {

    private XYSeries series;
    private String legendTitle;
    private final static XYSeriesFactory SERIES_FACTORY = new XYSeriesFactory();

    XYSeriesAdapter(final String legendTitle) {
        this.series = SERIES_FACTORY.newXYSeries(legendTitle);
        this.legendTitle = legendTitle;
    }

    private String legendTitle() {
        return this.legendTitle;
    }

    public XYSeries series() {
        return this.series;
    }

    @Override
    public void add(final Number x, final Number y) {
        series().add(x, y, false);
    }

    @Override
    public LineChartGraphData graphData(final Paint paint, final double range) {
        final LineChartGraphData data = new LineChartGraphData(legendTitle(), paint, series());
        return data.range(new Range(0, range));
    }
}
