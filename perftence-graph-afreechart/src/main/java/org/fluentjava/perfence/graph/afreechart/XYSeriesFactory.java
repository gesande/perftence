package org.fluentjava.perfence.graph.afreechart;

import org.afree.data.xy.XYSeries;

public class XYSeriesFactory {

    public XYSeries newXYSeries(final String legend) {
        return new XYSeries(legend, false, true);
    }
}
