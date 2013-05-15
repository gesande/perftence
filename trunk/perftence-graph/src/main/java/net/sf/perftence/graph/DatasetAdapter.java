package net.sf.perftence.graph;

import java.awt.Paint;

public interface DatasetAdapter<T> {
    public void add(final Number x, final Number y);

    T graphData(final Paint paint, final double range);
}
