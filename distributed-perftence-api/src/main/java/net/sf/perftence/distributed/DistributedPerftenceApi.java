package net.sf.perftence.distributed;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.concurrent.NamedThreadFactory;
import net.sf.perftence.fluent.DefaultRunNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DistributedPerftenceApi implements RunNotifier {

    private final static Logger LOG = LoggerFactory
            .getLogger(DistributedPerftenceApi.class);

    private final TestFailureNotifier testFailureNotifier;
    private final DistributedLatencyReporterFactory distributedLatencyReporterFactory;
    private final DefaultRunNotifier defaultRunNotifier = new DefaultRunNotifier();
    private ExecutorService executorService;
    private URL reportsTo;
    private Map<String, RemoteLatencyReporter> reporters = new HashMap<String, RemoteLatencyReporter>();

    public DistributedPerftenceApi(
            final TestFailureNotifier testFailureNotifier,
            final DistributedLatencyReporterFactory distributedLatencyReporterFactory) {
        this.testFailureNotifier = testFailureNotifier;
        this.distributedLatencyReporterFactory = distributedLatencyReporterFactory;
        this.executorService = newExecutorService(2);
    }

    public DistributedPerftenceApi reportingLatenciesTo(final URL reportsTo) {
        this.reportsTo = reportsTo;
        return this;
    }

    public DistributedPerftenceApi reportingThreads(final int threads) {
        this.executorService = newExecutorService(threads);
        return this;
    }

    private static ExecutorService newExecutorService(final int threads) {
        return Executors.newFixedThreadPool(threads,
                NamedThreadFactory.forNamePrefix("remote-reporter"));
    }

    public DistributedPerformanceTest test(final String id) {
        final RemoteLatencyReporter remoteReporter = reporterFactory()
                .forRemoteReporting(id, reportsTo());
        reporters().put(id, remoteReporter);
        return new DistributedPerformanceTest(id, this.testFailureNotifier,
                this.executorService, this, remoteReporter);
    }

    private Map<String, RemoteLatencyReporter> reporters() {
        return this.reporters;
    }

    private URL reportsTo() {
        return this.reportsTo;
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
        finished(id, reporters().get(id));
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