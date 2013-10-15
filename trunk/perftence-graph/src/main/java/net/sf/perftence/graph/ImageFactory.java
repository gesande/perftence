package net.sf.perftence.graph;

public interface ImageFactory {

	public void createXYLineChart(final String id, final ImageData imageData);

	public void createBarChart(final String id, final ImageData imageData);

	public void createScatterPlot(final String id, final ImageData imageData);

}
