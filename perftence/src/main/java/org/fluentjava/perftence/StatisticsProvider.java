package org.fluentjava.perftence;

public interface StatisticsProvider {

    long median();

    long duration();

    double throughput();

    long percentileLatency(final int percentile);

    long sampleCount();

    long maxLatency();

    long minLatency();

    double averageLatency();

    boolean hasSamples();

}
