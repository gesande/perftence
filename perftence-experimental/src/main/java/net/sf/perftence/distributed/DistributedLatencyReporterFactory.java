package net.sf.perftence.distributed;

import java.net.URL;

public interface DistributedLatencyReporterFactory {

    RemoteLatencyReporter forLocalReporting();

    RemoteLatencyReporter forRemoteReporting(final URL reportsTo);

}
