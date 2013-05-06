package net.sf.perftence.distributed;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.FailedInvocations;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.reporting.LatencyReporter;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DistributedPerftenceApi implements
        TestRuntimeReporterFactory {

    private final TestFailureNotifier testFailureNotifier;
    private final DistributedLatencyReporterFactory distributedLatencyReporterFactory;
    private LatencyReporter remoteReporter;
    private LatencyReporter localReporter;

    public DistributedPerftenceApi(
            final TestFailureNotifier testFailureNotifier,
            final DistributedLatencyReporterFactory distributedLatencyReporterFactory) {
        this.testFailureNotifier = testFailureNotifier;
        this.distributedLatencyReporterFactory = distributedLatencyReporterFactory;
    }

    public DistributedPerftenceApi reportsLatenciesTo(final URL reportsTo) {
        this.remoteReporter = reporterFactory().forRemoteReporting(reportsTo);
        return this;
    }

    public DistributedPerftenceApi reportingLocally() {
        this.localReporter = reporterFactory().forLocalReporting();
        return this;
    }

    public DistributedPerformanceTest test(final String id) {
        return new DistributedPerformanceTest(id, this.testFailureNotifier,
                this);
    }

    @Override
    public TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations) {
        return new DistributedTestRuntimeReporter(
                new DefaultTestRuntimeReporterFactory().newRuntimeReporter(
                        latencyProvider, includeInvocationGraph, setup,
                        failedInvocations), resolveReporters());
    }

    private LatencyReporter[] resolveReporters() {
        final List<LatencyReporter> reporters = new ArrayList<LatencyReporter>();
        addReporter(reporters, localReporter());
        addReporter(reporters, remoteReporter());
        return reporters.toArray(new LatencyReporter[reporters.size()]);
    }

    private LatencyReporter localReporter() {
        return this.localReporter;
    }

    private LatencyReporter remoteReporter() {
        return this.remoteReporter;
    }

    private static void addReporter(final List<LatencyReporter> reporters,
            final LatencyReporter reporter) {
        if (reporter != null) {
            reporters.add(reporter);
        }
    }

    private DistributedLatencyReporterFactory reporterFactory() {
        return this.distributedLatencyReporterFactory;
    }
}