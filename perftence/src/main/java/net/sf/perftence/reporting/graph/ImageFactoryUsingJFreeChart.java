package net.sf.perftence.reporting.graph;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.perftence.reporting.FileUtil;
import net.sf.perftence.reporting.HtmlReportDeployment;
import net.sf.perftence.reporting.Statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImageFactoryUsingJFreeChart implements ImageFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImageFactoryUsingJFreeChart.class);

    @Override
    public void createXYLineChart(final String id, final ImageData imageData) {
        log().info("Create line chart for: {}", id);
        final JFreeChart chart = newXYLineChart(imageData);
        final XYPlot plot = chart.getXYPlot();
        log().info("Processing data...");
        final LineChartGraphData mainData = lineChartGraphData(imageData,
                Color.GRAY);
        if (imageData.hasStatistics()) {
            addStatisticsGraphDataToPlot(plot, statistics(imageData, mainData),
                    0, mainData.range());
            addMainDataGraphToPlot(plot, mainData, 1);
        } else {
            addMainDataGraphToPlot(plot, mainData, 0);
        }
        writeToFile(id, chart);
    }

    @Override
    public void createBarChart(final String id, final ImageData imageData) {
        log().info("Create bar chart for:  {}", id);
        final JFreeChart chart = newBarChart(imageData);
        final CategoryPlot plot = chart.getCategoryPlot();
        log().info("Processing data...");
        final BarChartGraphData mainData = barGraphData(imageData, Color.GRAY);
        addMainDataGraphToPlot(plot, mainData, 0);
        writeToFile(id, chart);
    }

    @Override
    public void createScatterPlot(final String id, final ImageData imageData) {
        log().info("Create line chart for: {}", id);
        writeToFile(id, newScatterPlot(imageData));
    }

    private static JFreeChart newScatterPlot(final ImageData imageData) {
        final XYSeriesAdapterForScatterPlot adapter = (XYSeriesAdapterForScatterPlot) imageData
                .adapter();
        final ScatterPlotGraphData graphData = adapter.graphData(Color.RED, 0);
        return ChartFactory.createScatterPlot(imageData.title(),
                imageData.xAxisLabel(), graphData.yAxisTitle(),
                graphData.dataset(), PlotOrientation.VERTICAL, showLegend(),
                noTooltips(), noUrls());
    }

    private static void addMainDataGraphToPlot(final CategoryPlot plot,
            final BarChartGraphData data, final int index) {
        log().info("Processing main data...");
        final Paint paint = data.paint();
        final CategoryDataset dataset = data.categoryDataset();
        final Axis axis = new NumberAxis(data.title());
        axis.setLabelPaint(paint);
        axis.setTickLabelPaint(paint);
        log().debug("Setting axis '{}' range to '{}'", data.title(),
                data.range());
        ((ValueAxis) axis).setRange(data.range());

        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, (ValueAxis) axis);
        plot.mapDatasetToRangeAxis(index, index);

        log().info("Main data processed.");
    }

    private static void addStatisticsGraphDataToPlot(final XYPlot plot,
            final LineGraphStatisticsGraphData statistics, final int index,
            final Range range) {
        log().info("Processing statistics ...");
        XYSeriesCollection dataset = newXYSeriesCollection();
        StandardXYItemRenderer renderer = newXYItemRenderer();
        final Axis axis = newNumberAxis(statistics.title());
        axis.setLabelPaint(statistics.paint());
        axis.setTickLabelPaint(statistics.paint());
        log().debug("Setting statistics axis '{}' range to '{}'",
                statistics.title(), range);
        ((ValueAxis) axis).setRange(range);
        int i = 0;
        log().info("Processing statistics data series...");
        for (final LineChartGraphData data : statistics.values()) {
            dataset.addSeries(data.series());
            renderer.setSeriesPaint(i, data.paint());
            i++;
        }
        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, (ValueAxis) axis);
        plot.setRenderer(index, renderer);
        plot.mapDatasetToRangeAxis(index, 0);
        log().info("Statistics processed...");
    }

    private static Axis newNumberAxis(final String title) {
        return new NumberAxis(title);
    }

    private static XYSeriesCollection newXYSeriesCollection() {
        return new XYSeriesCollection();
    }

    private static LineGraphStatisticsGraphData statistics(
            final ImageData imageData,
            final LineChartGraphData lineChartGraphData) {
        log().debug("Gathering statistics");
        Statistics statistics = imageData.statistics();
        final double median = statistics.median();
        final double meanValue = statistics.mean();
        final double percentile95Value = statistics.percentile95();

        final LineGraphStatisticsGraphData data = new LineGraphStatisticsGraphData();

        final LineChartGraphData average = data.newSeries("Median",
                Color.GREEN, lineChartGraphData.range());
        final LineChartGraphData percentile95 = data.newSeries("95 percentile",
                Color.CYAN, lineChartGraphData.range());
        final LineChartGraphData mean = data.newSeries("Mean", Color.RED,
                lineChartGraphData.range());
        log().debug("Creating statistics data series...");
        for (int i = 0; i < lineChartGraphData.size(); i++) {
            average.addSeries(i, median);
            mean.addSeries(i, meanValue);
            percentile95.addSeries(i, percentile95Value);
        }
        log().debug("Statistics graph data ready.");
        return data;
    }

    private static void addMainDataGraphToPlot(final XYPlot plot,
            final LineChartGraphData data, final int index) {
        log().info("Processing main data...");
        Paint paint = data.paint();
        XYSeriesCollection dataset = newXYSeriesCollection();
        dataset.addSeries(data.series());
        Axis axis = new NumberAxis(data.title());
        axis.setLabelPaint(paint);
        axis.setTickLabelPaint(paint);
        log().debug("Setting axis '{}' range to '{}'", data.title(),
                data.range());
        ((ValueAxis) axis).setRange(data.range());

        StandardXYItemRenderer renderer = newXYItemRenderer();
        renderer.setSeriesPaint(0, paint);

        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, (ValueAxis) axis);
        plot.setRenderer(index, renderer);
        plot.mapDatasetToRangeAxis(index, index);
        log().info("Main data processed.");
    }

    private static StandardXYItemRenderer newXYItemRenderer() {
        return new StandardXYItemRenderer();
    }

    private static void writeToFile(final String id, final JFreeChart chart) {
        final String outputFilePath = reportDeploymentDirectory() + "/" + id
                + ".png";
        log().info("Writing to file {}", outputFilePath);
        try {
            FileUtil.ensureDirectoryExists(newFile(reportDeploymentDirectory()));
            ChartUtilities.saveChartAsPNG(newFile(outputFilePath), chart,
                    width(), height());
            log().info("Image successfully written to {}", outputFilePath);
        } catch (Exception e) {
            throw writingFileFailed(outputFilePath, e);
        }
    }

    private static File newFile(final String path) {
        return new File(path);
    }

    private static int height() {
        return 500;
    }

    private static int width() {
        return 800;
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

    private static BarChartGraphData barGraphData(final ImageData imageData,
            Paint paint) {
        return (BarChartGraphData) imageData.adapter().graphData(paint,
                imageData.range());
    }

    private static LineChartGraphData lineChartGraphData(
            final ImageData imageData, Paint paint) {
        return (LineChartGraphData) imageData.adapter().graphData(paint,
                imageData.range());
    }

    private static JFreeChart newBarChart(final ImageData imageData) {
        return ChartFactory.createBarChart(imageData.title(),
                imageData.xAxisLabel(), null, null, PlotOrientation.VERTICAL,
                showLegend(), noTooltips(), noUrls());
    }

    private static JFreeChart newXYLineChart(final ImageData imageData) {
        log().info("Create XY linechart...");
        return ChartFactory.createXYLineChart(imageData.title(),
                imageData.xAxisLabel(), null, null, PlotOrientation.VERTICAL,
                showLegend(), noTooltips(), noUrls());
    }

    static class LineGraphStatisticsGraphData {
        private Map<String, LineChartGraphData> list = new HashMap<String, LineChartGraphData>();

        LineGraphStatisticsGraphData() {
        }

        @SuppressWarnings("static-method")
        public Paint paint() {
            return Color.BLACK;
        }

        @SuppressWarnings("static-method")
        public String title() {
            return "Statistics";
        }

        public List<LineChartGraphData> values() {
            return new ArrayList<LineChartGraphData>(this.list.values());
        }

        LineChartGraphData newSeries(final String title, final Paint paint,
                Range range) {
            final LineChartGraphData value = new LineChartGraphData(title,
                    paint);
            value.range(range);
            this.list.put(title, value);
            return value;
        }
    }

    @Override
    public void updateIndexFile(final String id) {
        writeImageRefToTheIndexFile(reportDeploymentDirectory() + "/"
                + "index.html",
                "<a href=" + HtmlReportDeployment.reportAsHtml(id) + ">" + id
                        + "</a><br />");
    }

    private static void writeImageRefToTheIndexFile(final String indexFile,
            String link) {
        try {
            OutputStream out = new FileOutputStream(indexFile, true);
            out.write(link.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            throw writingImageRefFailed(link, e);
        } catch (IOException e) {
            throw writingImageRefFailed(link, e);
        }
        log().info("Index file {} updated", indexFile);
    }

    private static RuntimeException writingFileFailed(
            final String outputFilePath, Exception e) {
        return newRuntimeException("Writing file " + outputFilePath
                + " failed!", e);
    }

    private static RuntimeException writingImageRefFailed(final String link,
            Throwable t) {
        return newRuntimeException("Couldn't write image ref '" + link
                + "' to the index file!", t);
    }

    private static String reportDeploymentDirectory() {
        return HtmlReportDeployment.deploymentDirectory();
    }

    private static RuntimeException newRuntimeException(final String message,
            final Throwable t) {
        return FileUtil.newRuntimeException(message, t);
    }

    private static Logger log() {
        return LOGGER;
    }
}
