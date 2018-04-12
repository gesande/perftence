package org.fluentjava.perftence.common;

import org.fluentjava.perftence.graph.ImageData;

public interface InvocationStorage {

    void store(final int latency);

    Statistics statistics();

    boolean isEmpty();

    ImageData imageData();

    boolean reportedLatencyBeingBelowOne();
}
