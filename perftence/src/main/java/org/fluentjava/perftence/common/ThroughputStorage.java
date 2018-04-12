package org.fluentjava.perftence.common;

import org.fluentjava.perftence.graph.ImageData;

public interface ThroughputStorage {

    void store(final long currentDuration, final double throughput);

    boolean isEmpty();

    ImageData imageData();

}
