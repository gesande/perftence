package net.sf.perftence.reporting;

import net.sf.perftence.reporting.graph.ImageData;

public interface ThroughputStorage {

    void store(final long currentDuration, final double throughput);

    boolean isEmpty();

    ImageData imageData();

}
