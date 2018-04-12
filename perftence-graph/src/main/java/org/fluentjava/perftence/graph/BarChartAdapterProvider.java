package org.fluentjava.perftence.graph;

public interface BarChartAdapterProvider<GRAPHDATA, PAINT> {

    DatasetAdapter<GRAPHDATA, PAINT> forBarChart(final String legendTitle);

}
