package org.fluentjava.perftence.reporting;

public interface ThroughputReporter {
    void throughput(final long currentDuration, final double throughput);

}
