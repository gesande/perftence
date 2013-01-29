package net.sf.perftence.reporting.graph;

import org.jfree.data.xy.XYSeries;

@SuppressWarnings("static-method")
public class XYSeriesFactory {

    public XYSeries newXYSeries(final String legend) {
        return new XYSeries(legend, false, true);
    }
}
