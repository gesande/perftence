package net.sf.perftence.reporting;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.jfreechart.LineChartGraphData;

public final class FrequencyStorageFactory {

    private FrequencyStorageFactory() {
    }

    public static FrequencyStorage newFrequencyStorage(
            final LatencyProvider latencyProvider,
            final DatasetAdapterFactory datasetAdapterFactory) {
        return new FrequencyStorage() {

            @Override
            public ImageData imageData() {
                final String legendTitle = legendTitle();
                final ImageData imageData = newImageData(datasetAdapterFactory
                        .forLineChart(legendTitle));
                final long maxLatency = latencyProvider.maxLatency();
                long range = 0;
                for (long i = 0; i <= maxLatency; i++) {
                    long count = latencyProvider.latencyCount(i);
                    if (count > range) {
                        range = count;
                    }
                    imageData.add(i, count);
                }
                return imageData.range(range + 5.0);
            }

            @Override
            public boolean hasSamples() {
                return latencyProvider.hasSamples();
            }
        };
    }

    private static ImageData newImageData(
            final DatasetAdapter<LineChartGraphData> adapterForLinechart) {
        final ImageData imageData = ImageData.noStatistics(
                "Latency frequencies", "Latency (ms)", adapterForLinechart);
        return imageData;
    }

    private static String legendTitle() {
        return "Frequency";
    }
}
