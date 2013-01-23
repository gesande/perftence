package net.sf.perftence.reporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.graph.ScatterPlotGraphData;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(DefaultTestRunner.class)
public class ImageFactoryUsingJFreeChartTest extends AbstractMultiThreadedTest {
    private final static Logger LOG = LoggerFactory
            .getLogger(ImageFactoryUsingJFreeChartTest.class);
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static ImageFactoryUsingJFreeChart imageFactory;

    @BeforeClass
    public static void beforeClass() {
        imageFactory = new ImageFactoryUsingJFreeChart();
    }

    private static InvocationStorage newDefaultInvocationStorage(final int value) {
        log().info("Warming up invocation storage...");
        final InvocationStorage storage = DefaultInvocationStorage
                .newDefaultStorage(value,
                        ReportingOptionsFactory.latencyOptionsWithStatistics(120));
        for (int i = 0; i < value; i++) {
            storage.store(randomValue());
        }
        log().info("Warmed up invocation storage...");
        return storage;
    }

    private static int randomValue() {
        return RANDOM.nextInt(100) + 1;
    }

    private static int storageSize() {
        return 100000;
    }

    @Test
    public void createImageWithStatistics() throws Exception {
        ImageData imageData = newDefaultInvocationStorage(storageSize())
                .imageData();
        start();
        imageFactory().createXYLineChart(id("with-statistics"), imageData);
        done();
    }

    @Ignore
    @Test
    public void hugeDataSet() {
        ImageData imageData = newDefaultInvocationStorage(20000000).imageData();
        start();
        imageFactory().createXYLineChart(id("huge-with-statistics"), imageData);
        done();
    }

    private void done() {
        log().info("Done: {}", fullyQualifiedMethodNameWithClassName());
    }

    private void start() {
        log().info("Start: {}", fullyQualifiedMethodNameWithClassName());
    }

    @Test
    public void createImageWithoutStatistics() throws Exception {
        ImageData imageDataWithoutStatistics = imageDataWithoutStatistics();
        start();
        imageFactory().createXYLineChart(id("without-statistics"),
                imageDataWithoutStatistics);
        done();
    }

    @Test
    public void createBarChart() {
        ImageData imageDataWithoutStatistics = imageDataWithSmallAmountOfData();
        start();
        imageFactory().createBarChart(id("barchart-without-statistics"),
                imageDataWithoutStatistics);
        done();
    }

    @Test
    public void createScatterPlot() {
        ImageData imageDataWithoutStatistics = imageDataForScatterPlot();
        start();
        imageFactory().createScatterPlot(id("scatterplot-without-statistics"),
                imageDataWithoutStatistics);
        done();
    }

    private static ImageData imageDataForScatterPlot() {
        final ImageData data = ImageData.noStatistics("Scatter Plot Title",
                "xTitle", "legendTitle",
                scatterPlotAdapter("legendTitle", "yAxisTitle"));
        Random r = new Random();
        for (int i = 0; i <= 100; i++) {
            double x = r.nextDouble();
            double y = r.nextDouble();
            data.add(x, y);
        }
        return data;
    }

    private static DatasetAdapter<ScatterPlotGraphData> scatterPlotAdapter(
            final String legendTitle, final String yAxisTitle) {
        return DatasetAdapterFactory.adapterForScatterPlot(legendTitle,
                yAxisTitle);
    }

    private static ImageData imageDataWithSmallAmountOfData() {
        log().info("Warming up latency counter...");
        final LatencyProvider latencyProvider = new LatencyProvider();
        latencyProvider.start();
        for (int i = 0; i < 100; i++) {
            latencyProvider.addSample(200);
        }
        for (int i = 0; i < 10; i++) {
            latencyProvider.addSample(204);
        }
        for (int i = 0; i < 3; i++) {
            latencyProvider.addSample(403);
        }
        for (int i = 0; i < 5; i++) {
            latencyProvider.addSample(500);
        }
        latencyProvider.stop();

        return warmedUp(newStorage(toString(latencyProvider)).imageData());
    }

    private static InvocationStorage newStorage(
            final LatencyProvider latencyProvider) {
        return new InvocationStorage() {

            @Override
            public void store(int latency) {
                latencyProvider.addSample(latency);
            }

            @Override
            public Statistics statistics() {
                return Statistics.fromLatencies(new ArrayList<Integer>());
            }

            @Override
            public boolean reportedLatencyBeingBelowOne() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return !latencyProvider.hasSamples();
            }

            @Override
            public ImageData imageData() {
                final ImageData data = ImageData
                        .noStatistics("title", "xAxisTitle", "legendTitle",
                                DatasetAdapterFactory
                                        .adapterForBarChart("legendTitle"));
                final Collection<Long> samples = latencyProvider
                        .uniqueSamples();
                for (final Long sample : samples) {
                    data.add(latencyProvider.latencyCount(sample), sample);
                }
                data.range(latencyProvider.sampleCount());
                return data;
            }
        };
    }

    private static ImageData imageDataWithoutStatistics() {
        log().info("Warming up latency counter...");
        return warmedUp(FrequencyStorageFactory.newFrequencyStorage(
                toString(newCounterWithRandomContent(storageSize())))
                .imageData());
    }

    private static Logger log() {
        return LOG;
    }

    private static ImageData warmedUp(final ImageData imageData) {
        log().info("Warmed up...");
        return imageData;
    }

    private static LatencyProvider toString(
            final LatencyProvider latencyProvider) {
        log().info("newProvider = {}", latencyProvider.toString());
        return latencyProvider;
    }

    private static LatencyProvider newCounterWithRandomContent(final int size) {
        final LatencyProvider latencyProvider = new LatencyProvider();
        latencyProvider.start();
        for (int i = 0; i < size; i++) {
            final int n = RANDOM.nextInt(1000) + 1;
            final int latency = RANDOM.nextInt(n) + 1;
            latencyProvider.addSample(latency);
        }
        latencyProvider.stop();
        return latencyProvider;
    }

    private static ImageFactoryUsingJFreeChart imageFactory() {
        return imageFactory;
    }

    private static String id(String id) {
        return ImageFactoryUsingJFreeChartTest.class.getName() + "-" + id;
    }
}
