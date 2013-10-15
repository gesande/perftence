package net.sf.perftence.common;

import net.sf.perftence.graph.ImageData;

public interface ThroughputStorage {

	void store(final long currentDuration, final double throughput);

	boolean isEmpty();

	ImageData imageData();

}
