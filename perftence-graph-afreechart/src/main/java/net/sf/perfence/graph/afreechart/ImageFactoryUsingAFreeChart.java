package net.sf.perfence.graph.afreechart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afree.chart.AFreeChart;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.StandardXYItemRenderer;
import org.afree.data.Range;
import org.afree.data.category.CategoryDataset;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.GradientColor;
import org.afree.graphics.PaintType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.graph.ChartWriter;
import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.GraphStatisticsProvider;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.ImageFactory;

public final class ImageFactoryUsingAFreeChart implements ImageFactory {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageFactoryUsingAFreeChart.class);
	private final AFreeChartFactory aFreeChartFactory;
	private final ChartColors colors;
	private final ChartWriter<AFreeChart> writer;

	public ImageFactoryUsingAFreeChart(final ChartColors colors,
			final ChartWriter<AFreeChart> writer) {
		this.colors = colors;
		this.writer = writer;
		this.aFreeChartFactory = new AFreeChartFactory(colors);
	}

	@Override
	public void createXYLineChart(final String id, final ImageData imageData) {
		log().info("Create line chart for: {}", id);
		final AFreeChart chart = afreeChartFactory().newXYLineChart(imageData);
		final XYPlot plot = chart.getXYPlot();
		log().info("Processing data...");
		final LineChartGraphData mainData = lineChartGraphData(imageData,
				gray());
		if (imageData.hasStatistics()) {
			addStatisticsGraphDataToPlot(plot, statistics(imageData, mainData),
					0, mainData.range());
			addMainDataGraphToPlot(plot, mainData, 1);
		} else {
			addMainDataGraphToPlot(plot, mainData, 0);
		}
		writeChart(id, chart);
	}

	@Override
	public void createBarChart(final String id, final ImageData imageData) {
		log().info("Create bar chart for:  {}", id);
		final AFreeChart chart = afreeChartFactory().newBarChart(imageData);
		final CategoryPlot plot = chart.getCategoryPlot();
		log().info("Processing data...");
		final BarChartGraphData mainData = barGraphData(imageData, gray());
		addMainDataGraphToPlot(plot, mainData, 0);
		writeChart(id, chart);
	}

	@Override
	public void createScatterPlot(final String id, final ImageData imageData) {
		log().info("Create scatter plot for: {}", id);
		writeChart(id, afreeChartFactory().newScatterPlot(imageData));
	}

	private AFreeChartFactory afreeChartFactory() {
		return this.aFreeChartFactory;
	}

	private static void addMainDataGraphToPlot(final CategoryPlot plot,
			final BarChartGraphData data, final int index) {
		log().info("Processing main data...");
		final PaintType paintType = data.paint();
		final CategoryDataset dataset = data.categoryDataset();
		final ValueAxis axis = new NumberAxis(data.title());
		axis.setLabelPaintType(paintType);
		axis.setTickLabelPaintType(paintType);
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
		axis.setLabelPaintType(statistics.paint());
		axis.setTickLabelPaintType(statistics.paint());
		log().debug("Setting statistics axis '{}' range to '{}'",
				statistics.title(), range);
		axis.setRange(range);
		int i = 0;
		log().info("Processing statistics data series...");
		for (final LineChartGraphData data : statistics.values()) {
			dataset.addSeries(data.series());
			renderer.setSeriesPaintType(i, data.paint());
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

	private LineGraphStatisticsGraphData statistics(final ImageData imageData,
			final LineChartGraphData lineChartGraphData) {
		log().debug("Gathering statistics");
		final GraphStatisticsProvider statistics = imageData.statistics();
		final double median = statistics.median();
		final double meanValue = statistics.mean();
		final double percentile95Value = statistics.percentile95();
		final LineGraphStatisticsGraphData data = new LineGraphStatisticsGraphData();
		final LineChartGraphData average = data.newSeries("Median", green(),
				lineChartGraphData.range());
		final LineChartGraphData percentile95 = data.newSeries("95 percentile",
				cyan(), lineChartGraphData.range());
		final LineChartGraphData mean = data.newSeries("Mean", red(),
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
		PaintType paint = data.paint();
		final XYSeriesCollection dataset = newXYSeriesCollection();
		dataset.addSeries(data.series());
		final ValueAxis axis = new NumberAxis(data.title());
		axis.setLabelPaintType(paint);
		axis.setTickLabelPaintType(paint);
		log().debug("Setting axis '{}' range to '{}'", data.title(),
				data.range());
		axis.setRange(data.range());

		StandardXYItemRenderer renderer = newXYItemRenderer();
		renderer.setSeriesPaintType(0, new GradientColor());// paint);

		plot.setDataset(index, dataset);
		plot.setRangeAxis(index, axis);
		plot.setRenderer(index, renderer);
		plot.mapDatasetToRangeAxis(index, index);
		log().info("Main data processed.");
	}

	private static StandardXYItemRenderer newXYItemRenderer() {
		return new StandardXYItemRenderer();
	}

	private void writeChart(final String id, final AFreeChart chart) {
		this.writer.write(id, chart, 500, 800);
	}

	@SuppressWarnings("unchecked")
	private static BarChartGraphData barGraphData(final ImageData imageData,
			PaintType paint) {
		final DatasetAdapter<BarChartGraphData, PaintType> adapter = (DatasetAdapter<BarChartGraphData, PaintType>) imageData
				.adapter();
		return adapter.graphData(paint, imageData.range());
	}

	@SuppressWarnings("unchecked")
	private static LineChartGraphData lineChartGraphData(
			final ImageData imageData, final PaintType paint) {
		final DatasetAdapter<LineChartGraphData, PaintType> adapter = (DatasetAdapter<LineChartGraphData, PaintType>) imageData
				.adapter();
		return adapter.graphData(paint, imageData.range());
	}

	final class LineGraphStatisticsGraphData {
		private Map<String, LineChartGraphData> list = new HashMap<>();

		LineGraphStatisticsGraphData() {
		}

		public PaintType paint() {
			return colors().black();
		}

		public String title() {
			return "Statistics";
		}

		public List<LineChartGraphData> values() {
			return new ArrayList<>(this.list.values());
		}

		LineChartGraphData newSeries(final String title, final PaintType paint,
				Range range) {
			final LineChartGraphData value = new LineChartGraphData(title,
					paint);
			value.range(range);
			this.list.put(title, value);
			return value;
		}
	}

	private PaintType red() {
		return colors().red();
	}

	private ChartColors colors() {
		return this.colors;
	}

	private PaintType cyan() {
		return colors().cyan();
	}

	private PaintType green() {
		return colors().green();
	}

	private PaintType gray() {
		return colors().gray();
	}

	private static Logger log() {
		return LOGGER;
	}

}
