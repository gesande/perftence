package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.GraphData;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;

public final class LineChartGraphData implements GraphData<LineChartGraphData, Range, Paint> {

    private final XYSeries series;
    private final String title;
    private Range range;
    private Paint paint;
    private final static XYSeriesFactory SERIES_FACTORY = new XYSeriesFactory();

    LineChartGraphData(final String legendTitle, final Paint paint) {
        this.title = legendTitle;
        this.paint = paint;
        this.series = SERIES_FACTORY.newXYSeries(legendTitle);
        this.range = null;
    }

    LineChartGraphData(final String title, final Paint paint, final XYSeries series) {
        this.series = series;
        this.paint = paint;
        this.title = title;
    }

    @Override
    public Paint paint() {
        return this.paint;
    }

    public void addSeries(final Number x, final Number y) {
        series().add(x, y, false);
    }

    public XYSeries series() {
        return this.series;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public LineChartGraphData range(final Range range) {
        this.range = range;
        return this;
    }

    @Override
    public Range range() {
        return this.range;
    }

    public int size() {
        return series().getItemCount();
    }
}