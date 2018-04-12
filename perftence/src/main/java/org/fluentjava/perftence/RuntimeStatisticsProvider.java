package org.fluentjava.perftence;

public interface RuntimeStatisticsProvider {

    long currentDuration();

    double currentThroughput();

    long sampleCount();

    long median();

    long maxLatency();

    long minLatency();

    long percentileLatency(final int percentile);

    boolean hasSamples();

    double averageLatency();

}
