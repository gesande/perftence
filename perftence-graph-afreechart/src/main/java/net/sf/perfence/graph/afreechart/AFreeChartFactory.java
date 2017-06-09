package net.sf.perfence.graph.afreechart;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PlotOrientation;
import org.afree.graphics.PaintType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.graph.ImageData;

final class AFreeChartFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AFreeChartFactory.class);
    private final ChartColors chartColors;

    public AFreeChartFactory(final ChartColors chartColors) {
        this.chartColors = chartColors;
    }

    @SuppressWarnings("static-method")
    public AFreeChart newBarChart(final ImageData imageData) {
        LOGGER.info("Create bar chart...");
        return ChartFactory.createBarChart(imageData.title(), imageData.xAxisLabel(), null, null,
                PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
    }

    public AFreeChart newScatterPlot(final ImageData imageData) {
        LOGGER.info("Create scatter plot...");
        final XYSeriesAdapterForScatterPlot adapter = (XYSeriesAdapterForScatterPlot) imageData.adapter();
        final ScatterPlotGraphData graphData = adapter.graphData(red(), 0);
        return ChartFactory.createScatterPlot(imageData.title(), imageData.xAxisLabel(), graphData.yAxisTitle(),
                graphData.dataset(), PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
    }

    @SuppressWarnings("static-method")
    public AFreeChart newXYLineChart(final ImageData imageData) {
        LOGGER.info("Create XY linechart...");
        return ChartFactory.createXYLineChart(imageData.title(), imageData.xAxisLabel(), null, null,
                PlotOrientation.VERTICAL, showLegend(), noTooltips(), noUrls());
    }

    private PaintType red() {
        return this.chartColors.red();
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