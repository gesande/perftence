package net.sf.perfence.graph.afreechart;

import java.awt.Color;

import net.sf.perftence.graph.ImageData;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PlotOrientation;

final class AFreeChartFactory {
    @SuppressWarnings("static-method")
    public AFreeChart newBarChart(final ImageData imageData) {
        return ChartFactory.createBarChart(imageData.title(),
                imageData.xAxisLabel(), null, null, PlotOrientation.VERTICAL,
                showLegend(), noTooltips(), noUrls());
    }

    @SuppressWarnings("static-method")
    public AFreeChart newScatterPlot(final ImageData imageData) {
        final XYSeriesAdapterForScatterPlot adapter = (XYSeriesAdapterForScatterPlot) imageData
                .adapter();
        final ScatterPlotGraphData graphData = adapter.graphData(Color.RED, 0);
        return ChartFactory.createScatterPlot(imageData.title(),
                imageData.xAxisLabel(), graphData.yAxisTitle(),
                graphData.dataset(), PlotOrientation.VERTICAL, showLegend(),
                noTooltips(), noUrls());
    }

    @SuppressWarnings("static-method")
    public AFreeChart newXYLineChart(final ImageData imageData) {
        ImageFactoryUsingAFreeChart.log().info("Create XY linechart...");
        return ChartFactory.createXYLineChart(imageData.title(),
                imageData.xAxisLabel(), null, null, PlotOrientation.VERTICAL,
                showLegend(), noTooltips(), noUrls());
    }

    private static boolean showLegend() {
        return true;
    }

    private static boolean noTooltips() {
        return false;
    }

    private static boolean noUrls() {
        return false;
    }

}