package net.sf.perftence.graph.jfreechart;

import net.sf.perftence.graph.DatasetAdapter;

public interface ScatterPlotAdapterProvider<GRAPHDATA, PAINT> {

    DatasetAdapter<GRAPHDATA, PAINT> forScatterPlot(final String legendTitle,
            final String yAxisTitle);

}
