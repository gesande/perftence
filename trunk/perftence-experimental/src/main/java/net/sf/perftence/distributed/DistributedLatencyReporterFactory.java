package net.sf.perftence.distributed;

import java.net.URL;

import net.sf.perftence.reporting.LatencyReporter;

public interface DistributedLatencyReporterFactory {

    LatencyReporter forLocalReporting();

    LatencyReporter forRemoteReporting(final URL reportsTo);

}
