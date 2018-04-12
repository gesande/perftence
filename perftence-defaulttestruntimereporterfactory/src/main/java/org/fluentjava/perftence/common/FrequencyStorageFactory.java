package org.fluentjava.perftence.common;

import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.graph.DatasetAdapter;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;

public final class FrequencyStorageFactory {

    private FrequencyStorageFactory() {
    }

    public static FrequencyStorage newFrequencyStorage(final LatencyProvider latencyProvider,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        return new FrequencyStorage() {

            @Override
            public ImageData imageData() {
                final ImageData imageData = newImageData(lineChartAdapterProvider.forLineChart(legendTitle()));
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

    private static ImageData newImageData(final DatasetAdapter<?, ?> adapterForLinechart) {
        return ImageData.noStatistics("Latency frequencies", "Latency (ms)", adapterForLinechart);
    }

    private static String legendTitle() {
        return "Frequency";
    }
}
