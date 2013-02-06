package net.sf.perftence;

import java.util.Collection;

import net.sf.perftence.bag.StronglyTypedSortedBag;

public final class LatencyProvider implements StatisticsProvider,
        RuntimeStatisticsProvider, TestTimeAware {

    private final StronglyTypedSortedBag<Long> latencies;

    private long startTime;
    private long endTime;
    private long totalLatency;

    private LatencyProvider(final StronglyTypedSortedBag<Long> latencies) {
        this.latencies = latencies;
        init();
    }

    public static LatencyProvider withSynchronized() {
        return new LatencyProvider(
                StronglyTypedSortedBag.<Long> synchronizedTreeBag());
    }

    private void init() {
        this.totalLatency = 0;
        this.startTime = -1;
        this.endTime = -1;
    }

    public void start() {
        init();
        latencies().clear();
        this.startTime = System.currentTimeMillis();
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
        this.endTime = System.currentTimeMillis();
    }

    @Override
    public long startTime() {
        return this.startTime;
    }

    public long latencyCount(final long latency) {
        return latencies().count(latency);
    }

    @Override
    public double averageLatency() {
        return (double) this.totalLatency / sampleCount();
    }

    @Override
    public long minLatency() {
        final Long first = findFirst();
        return first == null ? 0 : first;
    }

    private Long findFirst() {
        return latencies().findFirst();
    }

    @Override
    public long maxLatency() {
        final Long last = findLast();
        return last == null ? 0 : last;
    }

    private Long findLast() {
        return latencies().findLast();
    }

    @Override
    public long sampleCount() {
        return latencies().size();
    }

    private StronglyTypedSortedBag<Long> latencies() {
        return this.latencies;
    }

    @Override
    public long percentileLatency(final int percentile) {
        final long targetCount = percentile * sampleCount() / 100;
        long count = 0;
        for (long value = minLatency(); value <= maxLatency(); value++) {
            count += latencyCount(value);
            if (count >= targetCount) {
                return value;
            }
        }
        return maxLatency();
    }

    @Override
    public double throughput() {
        if (this.startTime == -1 || this.endTime == -1) {
            throw new IllegalArgumentException(
                    "Invalid state: Use start() and stop() to indicate test start and end!");
        }
        return calculateThroughput(sampleCount(), duration());
    }

    private static double calculateThroughput(final long sampleCount,
            final long duration) {
        return 1000. * sampleCount / duration;
    }

    @Override
    public double currentThroughput() {
        return calculateThroughput(sampleCount(), currentDuration());
    }

    @Override
    public long duration() {
        return this.endTime - this.startTime;
    }

    @Override
    public String toString() {
        return latencies().toString();
    }

    public Collection<Long> uniqueSamples() {
        return latencies().uniqueSamples();
    }

    @Override
    public long median() {
        return percentileLatency(50);
    }

    @Override
    public long currentDuration() {
        return System.currentTimeMillis() - startTime();
    }
}
