package net.sf.perftence.distributed;

import net.sf.perftence.reporting.LatencyReporter;

public interface RemoteLatencyReporter extends LatencyReporter {
	void finished(final String id);
}
