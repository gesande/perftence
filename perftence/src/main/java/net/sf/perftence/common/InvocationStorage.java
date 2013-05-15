package net.sf.perftence.common;

import net.sf.perftence.graph.ImageData;

public interface InvocationStorage {

    void store(final int latency);

    Statistics statistics();

    boolean isEmpty();

    ImageData imageData();

    boolean reportedLatencyBeingBelowOne();
}
