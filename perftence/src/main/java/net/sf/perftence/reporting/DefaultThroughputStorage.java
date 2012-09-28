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

    public DefaultThroughputStorage(final int range) {
        this.range = range;
        this.list = Collections.synchronizedList(new ArrayList<Double>());
        this.time = Collections.synchronizedList(new ArrayList<Long>());
    }

    @Override
    public synchronized void store(final long currentDuration,
            final double throughput) {
        this.list.add(throughput);
        this.time.add(currentDuration);
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public ImageData imageData() {
        final String legendTitle = "Throughput";
        final ImageData imageData = ImageData.noStatistics(
                "Throughput over time", "Time elapsed", legendTitle, range(),
                DatasetAdapterFactory.adapterForLineChart(legendTitle));
        int i = 0;
        for (double latency : this.list) {
            imageData.add(this.time.get(i), latency);
            i++;
        }
        return imageData;

    }

    private int range() {
        return this.range;
    }

}
