package org.fluentjava.perfence.graph.afreechart;

import org.afree.data.xy.XYDataset;

public final class ScatterPlotGraphData {

    private final XYDataset result;
    private final String yAxisTitle;

    ScatterPlotGraphData(final String yAxisTitle, final XYDataset result) {
        this.yAxisTitle = yAxisTitle;
        this.result = result;
    }

    public XYDataset dataset() {
        return this.result;
    }

    public String yAxisTitle() {
        return this.yAxisTitle;
    }
}