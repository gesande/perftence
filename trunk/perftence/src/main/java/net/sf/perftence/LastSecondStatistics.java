package net.sf.perftence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LastSecondStatistics implements RuntimeStatisticsProvider,
        CustomInvocationReporter {

    private final Map<Long, LatencyProvider> latencies = Collections
            .synchronizedMap(new HashMap<Long, LatencyProvider>());

    @Override
    public synchronized void latency(final int latency) {
        final long currentSecond = currentSecond();
        if (latencies().containsKey(currentSecond)) {
            latencies().get(currentSecond).addSample(latency);
        } else {
            final LatencyProvider latencyProvider = new LatencyProvider();
            latencyProvider.start();
            latencyProvider.addSample(latency);
            latencies().put(currentSecond, latencyProvider);
        }
    }

    private Map<Long, LatencyProvider> latencies() {
        return this.latencies;
    }

    private static long currentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public synchronized long currentDuration() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).currentDuration();
        }
        return 0;
    }

    private static long lastSecond() {
        return currentSecond() - 1;
    }

    @Override
    public synchronized double currentThroughput() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).currentThroughput();
        }
        return 0;
    }

    @Override
    public synchronized long sampleCount() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).sampleCount();
        }
        return 0;
    }

    @Override
    public synchronized long median() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).median();
        }
        return 0;
    }

    @Override
    public synchronized long maxLatency() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).maxLatency();
        }
        return 0;
    }

    @Override
    public synchronized long minLatency() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).minLatency();
        }
        return 0;
    }

    @Override
    public synchronized long percentileLatency(final int percentile) {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).percentileLatency(percentile);
        }
        return 0;
    }

    @Override
    public synchronized boolean hasSamples() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).hasSamples();
        }
        return false;
    }

    @Override
    public synchronized double averageLatency() {
        final long currentSecond = lastSecond();
        if (latencies().containsKey(currentSecond)) {
            return latencies().get(currentSecond).averageLatency();
        }
        return 0;
    }

}
