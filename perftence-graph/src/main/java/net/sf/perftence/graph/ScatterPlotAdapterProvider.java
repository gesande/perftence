package net.sf.perftence.graph;

public interface ScatterPlotAdapterProvider<GRAPHDATA, PAINT> {

	DatasetAdapter<GRAPHDATA, PAINT> forScatterPlot(final String legendTitle,
			final String yAxisTitle);

}
