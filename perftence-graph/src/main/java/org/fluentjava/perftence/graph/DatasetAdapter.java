package org.fluentjava.perftence.graph;

public interface DatasetAdapter<T, PAINT> {
    public void add(final Number x, final Number y);

    T graphData(final PAINT paint, final double range);
}
