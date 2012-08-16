package net.sf.perftence;

import java.util.Collection;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.bag.TreeBag;

public final class LatencyProvider implements StatisticsProvider,
        RuntimeStatisticsProvider {

    private long startTime;
    private long endTime;
    private long totalLatency;
    private final SortedBag latencies;

    public LatencyProvider() {
        this.latencies = SynchronizedSortedBag.decorate(new TreeBag());
        init();
    }

    private void init() {
        this.totalLatency = 0;
        this.startTime = -1;
        this.endTime = -1;
    }

    public void start() {
        init();
        latencies().clear();
        this.startTime = System.nanoTime() / 1000000;
    }

    @Override
    public boolean hasSamples() {
        return sampleCount() > 0;
    }

    public synchronized void addSample(final long latency) {
        latencies().add(latency);
        this.totalLatency += latency;
    }

    public void stop() {
        this.endTime = System.nanoTime() / 1000000;
    }

    public long startTime() {
        return this.startTime;
    }

    public long latencyCount(final long latency) {
        return latencies().getCount(latency);
    }

    @Override
    public double averageLatency() {
        return (double) this.totalLatency / sampleCount();
    }

    @Override
    public long minLatency() {
        final Object first = findFirst();
        return first == null ? 0 : (Long) first;
    }

    private Object findFirst() {
        return latencies().isEmpty() ? null : latencies().first();
    }

    @Override
    public long maxLatency() {
        final Object last = findLast();
        return last == null ? 0 : (Long) last;
    }

    private Object findLast() {
        return latencies().isEmpty() ? null : latencies().last();
    }

    @Override
    public long sampleCount() {
        return latencies().size();
    }

    private SortedBag latencies() {
        return this.latencies;
    }

    @Override
    public long percentileLatency(final int percentile) {
        final long targetCount = percentile * sampleCount() / 100;
        long count = 0;
        for (long value = minLatency(); value <= maxLatency(); value++) {
            count += latencyCount(value);
            if (count >= targetCount)
                return value;
        }
        return maxLatency();
    }

    @Override
    public double throughput() {
        if (this.startTime == -1 || this.endTime == -1)
            throw new IllegalArgumentException(
                    "Invalid setup: Use start() and stop() to indicate test start and end!");
        return currentThroughput(sampleCount(), duration());
    }

    private static double currentThroughput(final long sampleCount,
            final long duration) {
        return 1000. * sampleCount / duration;
    }

    @Override
    public double currentThroughput() {
        return currentThroughput(sampleCount(), currentDuration());
    }

    @Override
    public long duration() {
        return this.endTime - this.startTime;
    }

    @Override
    public String toString() {
        return latencies().toString();
    }

    @SuppressWarnings("unchecked")
    public Collection<Long> uniqueSamples() {
        return latencies().uniqueSet();
    }

    @Override
    public long median() {
        return percentileLatency(50);
    }

    @Override
    public long currentDuration() {
        return (System.nanoTime() / 1000000) - startTime();
    }
}
