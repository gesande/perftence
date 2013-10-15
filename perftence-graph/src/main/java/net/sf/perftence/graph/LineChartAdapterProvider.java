package net.sf.perftence.graph;

public interface LineChartAdapterProvider<GRAPHDATA, PAINT> {

	DatasetAdapter<GRAPHDATA, PAINT> forLineChart(String title);

}
