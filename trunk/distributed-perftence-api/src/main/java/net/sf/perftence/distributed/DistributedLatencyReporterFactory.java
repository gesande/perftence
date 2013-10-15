package net.sf.perftence.distributed;

import java.net.URL;

public interface DistributedLatencyReporterFactory {

	RemoteLatencyReporter forRemoteReporting(final String id,
			final URL reportsTo);

}
