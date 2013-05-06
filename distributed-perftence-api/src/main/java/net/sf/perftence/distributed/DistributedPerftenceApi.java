package net.sf.perftence.distributed;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.FailedInvocations;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.concurrent.NamedThreadFactory;
import net.sf.perftence.fluent.DefaultRunNotifier;
import net.sf.perftence.reporting.LatencyReporter;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.setup.PerformanceTestSetup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DistributedPerftenceApi implements
        TestRuntimeReporterFactory, RunNotifier {

    private final static Logger LOG = LoggerFactory
            .getLogger(DistributedPerftenceApi.class);

    private final TestFailureNotifier testFailureNotifier;
    private final DistributedLatencyReporterFactory distributedLatencyReporterFactory;
    private final DefaultRunNotifier defaultRunNotifier = new DefaultRunNotifier();
    private ExecutorService executorService;
    private RemoteLatencyReporter remoteReporter;
    private RemoteLatencyReporter localReporter;

    public DistributedPerftenceApi(
            final TestFailureNotifier testFailureNotifier,
            final DistributedLatencyReporterFactory distributedLatencyReporterFactory) {
        this.testFailureNotifier = testFailureNotifier;
        this.distributedLatencyReporterFactory = distributedLatencyReporterFactory;
        this.executorService = Executors.newFixedThreadPool(2,
                NamedThreadFactory.forNamePrefix("remote-reporter"));
    }

    public DistributedPerftenceApi reportingLatenciesTo(final URL reportsTo) {
        this.remoteReporter = reporterFactory().forRemoteReporting(reportsTo);
        return this;
    }

    public DistributedPerftenceApi reportingThreads(final int threads) {
        this.executorService = Executors.newFixedThreadPool(threads,
                NamedThreadFactory.forNamePrefix("remote-reporter"));
        return this;
    }

    public DistributedPerformanceTest test(final String id) {
        return new DistributedPerformanceTest(id, this.testFailureNotifier,
                this, this);
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
                        failedInvocations), this.executorService,
                resolveReporters());
    }

    private LatencyReporter[] resolveReporters() {
        final List<LatencyReporter> reporters = new ArrayList<LatencyReporter>();
        addReporter(reporters, localReporter());
        addReporter(reporters, remoteReporter());
        return reporters.toArray(new LatencyReporter[reporters.size()]);
    }

    private RemoteLatencyReporter localReporter() {
        return this.localReporter;
    }

    private RemoteLatencyReporter remoteReporter() {
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

    @Override
    public void finished(String id) {
        this.defaultRunNotifier.finished(id);
        reportersFinished(id);
        shutdownExecutorService();
    }

    private void shutdownExecutorService() {
        this.executorService.shutdown();
        LOG.info("Executor service for remote reporters has been shut down.");
    }

    private void reportersFinished(final String id) {
        finished(id, localReporter());
        finished(id, remoteReporter());
    }

    private static void finished(final String id,
            final RemoteLatencyReporter reporter) {
        if (reporter != null) {
            reporter.finished(id);
        }
    }

    @Override
    public boolean isFinished(String id) {
        return this.defaultRunNotifier.isFinished(id);
    }
}