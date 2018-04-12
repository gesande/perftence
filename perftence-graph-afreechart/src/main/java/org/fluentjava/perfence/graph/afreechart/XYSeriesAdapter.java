package org.fluentjava.perfence.graph.afreechart;

import org.afree.data.Range;
import org.afree.data.xy.XYSeries;
import org.afree.graphics.PaintType;
import org.fluentjava.perftence.graph.DatasetAdapter;

final class XYSeriesAdapter implements DatasetAdapter<LineChartGraphData, PaintType> {

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
    public LineChartGraphData graphData(final PaintType paint, final double range) {
        final LineChartGraphData data = new LineChartGraphData(legendTitle(), paint, series());
        return data.range(new Range(0, range));
    }
}
