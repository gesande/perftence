package net.sf.perftence.graph.jfreechart;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.perftence.graph.GraphStatisticsProvider;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.reporting.TestReport;
import net.sf.völundr.fileio.FileUtil;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
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
    private final TestReport testReport;
    private final JFreeChartFactory jFreeChartFactory;

    public ImageFactoryUsingJFreeChart(final TestReport testReport) {
        this.testReport = testReport;
        this.jFreeChartFactory = new JFreeChartFactory();
    }

    @Override
    public void createXYLineChart(final String id, final ImageData imageData) {
        log().info("Create line chart for: {}", id);
        final JFreeChart chart = jfreeChartFactory().newXYLineChart(imageData);
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
        writeChartToFile(id, chart);
    }

    @Override
    public void createBarChart(final String id, final ImageData imageData) {
        log().info("Create bar chart for:  {}", id);
        final JFreeChart chart = jfreeChartFactory().newBarChart(imageData);
        final CategoryPlot plot = chart.getCategoryPlot();
        log().info("Processing data...");
        final BarChartGraphData mainData = barGraphData(imageData, Color.GRAY);
        addMainDataGraphToPlot(plot, mainData, 0);
        writeChartToFile(id, chart);
    }

    @Override
    public void createScatterPlot(final String id, final ImageData imageData) {
        log().info("Create line chart for: {}", id);
        writeChartToFile(id, jfreeChartFactory().newScatterPlot(imageData));
    }

    private JFreeChartFactory jfreeChartFactory() {
        return this.jFreeChartFactory;
    }

    private static void addMainDataGraphToPlot(final CategoryPlot plot,
            final BarChartGraphData data, final int index) {
        log().info("Processing main data...");
        final Paint paint = data.paint();
        final CategoryDataset dataset = data.categoryDataset();
        final ValueAxis axis = new NumberAxis(data.title());
        axis.setLabelPaint(paint);
        axis.setTickLabelPaint(paint);
        log().debug("Setting axis '{}' range to '{}'", data.title(),
                data.range());
        axis.setRange(data.range());

        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, axis);
        plot.mapDatasetToRangeAxis(index, index);

        log().info("Main data processed.");
    }

    private static void addStatisticsGraphDataToPlot(final XYPlot plot,
            final LineGraphStatisticsGraphData statistics, final int index,
            final Range range) {
        log().info("Processing statistics ...");
        XYSeriesCollection dataset = newXYSeriesCollection();
        StandardXYItemRenderer renderer = newXYItemRenderer();
        final ValueAxis axis = newNumberAxis(statistics.title());
        axis.setLabelPaint(statistics.paint());
        axis.setTickLabelPaint(statistics.paint());
        log().debug("Setting statistics axis '{}' range to '{}'",
                statistics.title(), range);
        axis.setRange(range);
        int i = 0;
        log().info("Processing statistics data series...");
        for (final LineChartGraphData data : statistics.values()) {
            dataset.addSeries(data.series());
            renderer.setSeriesPaint(i, data.paint());
            i++;
        }
        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, axis);
        plot.setRenderer(index, renderer);
        plot.mapDatasetToRangeAxis(index, 0);
        log().info("Statistics processed...");
    }

    private static ValueAxis newNumberAxis(final String title) {
        return new NumberAxis(title);
    }

    private static XYSeriesCollection newXYSeriesCollection() {
        return new XYSeriesCollection();
    }

    private static LineGraphStatisticsGraphData statistics(
            final ImageData imageData,
            final LineChartGraphData lineChartGraphData) {
        log().debug("Gathering statistics");
        final GraphStatisticsProvider statistics = imageData.statistics();
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
        final XYSeriesCollection dataset = newXYSeriesCollection();
        dataset.addSeries(data.series());
        final ValueAxis axis = new NumberAxis(data.title());
        axis.setLabelPaint(paint);
        axis.setTickLabelPaint(paint);
        log().debug("Setting axis '{}' range to '{}'", data.title(),
                data.range());
        axis.setRange(data.range());

        StandardXYItemRenderer renderer = newXYItemRenderer();
        renderer.setSeriesPaint(0, paint);

        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, axis);
        plot.setRenderer(index, renderer);
        plot.mapDatasetToRangeAxis(index, index);
        log().info("Main data processed.");
    }

    private static StandardXYItemRenderer newXYItemRenderer() {
        return new StandardXYItemRenderer();
    }

    private void writeChartToFile(final String id, final JFreeChart chart) {
        final String outputFilePath = reportDeploymentDirectory() + "/" + id
                + ".png";
        log().info("Writing to file {}", outputFilePath);
        try {
            FileUtil.ensureDirectoryExists(newFile(reportDeploymentDirectory()));
            ChartUtilities.saveChartAsPNG(newFile(outputFilePath), chart,
                    width(), height());
            log().info("Image successfully written to {}", outputFilePath);
        } catch (final Exception e) {
            throw new RuntimeException(logError(outputFilePath, e), e);
        }
    }

    private static String logError(final String outputFilePath,
            final Throwable t) {
        final String errorMsg = "Writing file '" + outputFilePath + "' failed!";
        log().error(errorMsg, t);
        return errorMsg;
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

    final static class LineGraphStatisticsGraphData {
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

    private String reportDeploymentDirectory() {
        return testReport().reportRootDirectory();
    }

    static Logger log() {
        return LOGGER;
    }

    private TestReport testReport() {
        return this.testReport;
    }
}
