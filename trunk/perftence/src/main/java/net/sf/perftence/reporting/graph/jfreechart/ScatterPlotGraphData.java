package net.sf.perftence.reporting.graph.jfreechart;

import org.jfree.data.xy.XYDataset;

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