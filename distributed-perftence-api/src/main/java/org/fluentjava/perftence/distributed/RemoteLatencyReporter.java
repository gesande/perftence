package org.fluentjava.perftence.distributed;

import org.fluentjava.perftence.reporting.LatencyReporter;

public interface RemoteLatencyReporter extends LatencyReporter {
    void finished(final String id);
}
