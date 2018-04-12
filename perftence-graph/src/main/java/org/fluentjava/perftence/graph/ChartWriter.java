package org.fluentjava.perftence.graph;

public interface ChartWriter<CHART> {

    void write(final String id, final CHART chart, final int height, final int width);

}
