package net.sf.perftence.graph.jfreechart;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.graph.ImageData;

final class JFreeChartFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFactoryUsingJFreeChart.class);

    @SuppressWarnings("static-method")
    public JFreeChart newBarChart(final ImageData imageData) {
        LOGGER.info("Create bar chart...");
        return ChartFactory.createBarChart(imageData.title(), imageData.xAxisLabel(), null, null,
                PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
    }

    @SuppressWarnings("static-method")
    public JFreeChart newScatterPlot(final ImageData imageData) {
        LOGGER.info("Create scatter plot...");
        final XYSeriesAdapterForScatterPlot adapter = (XYSeriesAdapterForScatterPlot) imageData.adapter();
        final ScatterPlotGraphData graphData = adapter.graphData(Color.RED, 0);
        return ChartFactory.createScatterPlot(imageData.title(), imageData.xAxisLabel(), graphData.yAxisTitle(),
                graphData.dataset(), PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
    }

    @SuppressWarnings("static-method")
    public JFreeChart newXYLineChart(final ImageData imageData) {
        LOGGER.info("Create XY linechart...");
        return ChartFactory.createXYLineChart(imageData.title(), imageData.xAxisLabel(), null, null,
                PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
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