package net.sf.perftence.reporting.graph;

import java.awt.Paint;

public interface DatasetAdapter<T> {
    public void add(final Number x, final Number y);

    T graphData(final Paint paint, final double range);
}
