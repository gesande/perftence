package net.sf.perftence.graph.jfreechart;

import net.sf.perftence.graph.DatasetAdapter;

public interface BarChartAdapterProvider<GRAPHDATA, PAINT> {

    DatasetAdapter<GRAPHDATA, PAINT> forBarChart(final String legendTitle);

}
