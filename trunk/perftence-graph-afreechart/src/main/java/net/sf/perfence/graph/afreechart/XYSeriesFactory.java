package net.sf.perfence.graph.afreechart;

import org.afree.data.xy.XYSeries;

@SuppressWarnings("static-method")
public class XYSeriesFactory {

	public XYSeries newXYSeries(final String legend) {
		return new XYSeries(legend, false, true);
	}
}
