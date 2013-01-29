package net.sf.perftence.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.ImageData;

public final class DefaultThroughputStorage implements ThroughputStorage {
    private final List<Double> list;
    private final List<Long> time;
    private final int range;
    private final DatasetAdapterFactory datasetAdapterFactory;

    public DefaultThroughputStorage(final int range,
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.range = range;
        this.datasetAdapterFactory = datasetAdapterFactory;
        this.list = Collections.synchronizedList(new ArrayList<Double>());
        this.time = Collections.synchronizedList(new ArrayList<Long>());
    }

    @Override
    public synchronized void store(final long currentDuration,
            final double throughput) {
        throughputs().add(throughput);
        reportTimes().add(currentDuration);
    }

    @Override
    public boolean isEmpty() {
        return throughputs().isEmpty();
    }

    @Override
    public ImageData imageData() {
        final String legendTitle = "Throughput";
        final ImageData imageData = ImageData.noStatistics(
                "Throughput over time", "Time elapsed", range(),
                datasetAdapterFactory().forLineChart(legendTitle));
        int i = 0;
        for (final double latency : throughputs()) {
            imageData.add(reportTimes().get(i), latency);
            i++;
        }
        return imageData;
    }

    private List<Double> throughputs() {
        return this.list;
    }

    private List<Long> reportTimes() {
        return this.time;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    private int range() {
        return this.range;
    }

}
