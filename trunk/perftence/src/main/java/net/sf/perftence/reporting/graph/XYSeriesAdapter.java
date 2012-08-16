package net.sf.perftence.reporting.graph;

import java.awt.Paint;

import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;

final class XYSeriesAdapter implements DatasetAdapter<LineChartGraphData> {

    private XYSeries series;
    private String legendTitle;

    XYSeriesAdapter(final String legendTitle) {
        this.series = new XYSeries(legendTitle);
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
        final LineChartGraphData data = new LineChartGraphData(legendTitle(),
                paint, series());
        return data.range(new Range(0, range));
    }
}
