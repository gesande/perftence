package net.sf.perftence.reporting;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.LineChartGraphData;

import org.apache.commons.collections.Bag;

public final class FrequencyStorageFactory {

    private FrequencyStorageFactory() {
    }

    public static FrequencyStorage newFrequencyStorage(
            final LatencyProvider latencyProvider) {
        return new FrequencyStorage() {

            @Override
            public ImageData imageData() {
                final String legendTitle = legendTitle();
                final ImageData imageData = newImageData(legendTitle);
                final long maxLatency = latencyProvider.maxLatency();
                long range = 0;
                for (long i = 0; i <= maxLatency; i++) {
                    long count = latencyProvider.latencyCount(i);
                    if (count > range) {
                        range = count;
                    }
                    imageData.add(i, count);
                }
                return imageData.range(range);
            }

            @Override
            public boolean hasSamples() {
                return latencyProvider.hasSamples();
            }
        };
    }

    private static ImageData newImageData(final String legendTitle) {
        final ImageData imageData = ImageData.noStatistics(
                "Latency frequencies", "Latency (ms)", legendTitle,
                adapterForLinechart(legendTitle));
        return imageData;
    }

    private static String legendTitle() {
        return "Frequency";
    }

    private static DatasetAdapter<LineChartGraphData> adapterForLinechart(
            final String legendTitle) {
        return DatasetAdapterFactory.adapterForLineChart(legendTitle);
    }

    public static FrequencyStorage newFrequencyStorage(final Bag values) {
        return new FrequencyStorage() {
            @Override
            public ImageData imageData() {
                final String legendTitle = legendTitle();
                final ImageData imageData = newImageData(legendTitle);
                long range = 0;
                for (final Object value : values.uniqueSet()) {
                    final int count = values.getCount(value);
                    if (count > range) {
                        range = count;
                    }
                    imageData.add((Number) value, count);
                }
                return imageData.range(range);
            }

            @Override
            public boolean hasSamples() {
                return !values.isEmpty();
            }
        };
    }
}
