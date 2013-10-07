package net.sf.perftence.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.LineChartAdapterProvider;

public final class DefaultThroughputStorage implements ThroughputStorage {

    private final List<Double> list;
    private final List<Long> time;
    private final int range;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;

    public DefaultThroughputStorage(final int range,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        this.range = range;
        this.lineChartAdapterProvider = lineChartAdapterProvider;
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

    private LineChartAdapterProvider<?, ?> datasetAdapterFactory() {
        return this.lineChartAdapterProvider;
    }

    private int range() {
        return this.range;
    }

}
