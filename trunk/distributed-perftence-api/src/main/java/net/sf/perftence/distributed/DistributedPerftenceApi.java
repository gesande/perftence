package net.sf.perftence.distributed;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.fluent.DefaultRunNotifier;
import net.sf.völundr.concurrent.ExecutorServiceFactory;

public final class DistributedPerftenceApi {

	private final static Logger LOG = LoggerFactory
			.getLogger(DistributedPerftenceApi.class);

	private final TestFailureNotifier testFailureNotifier;
	private final DistributedLatencyReporterFactory distributedLatencyReporterFactory;
	private final DefaultRunNotifier defaultRunNotifier = new DefaultRunNotifier();
	private final ExecutorServiceFactory executorServiceFactory;
	private final Map<String, RemoteLatencyReporter> reporters = new HashMap<>();

	private ExecutorService executorService;
	private URL reportsTo;

	public DistributedPerftenceApi(
			final TestFailureNotifier testFailureNotifier,
			final DistributedLatencyReporterFactory distributedLatencyReporterFactory) {
		this.testFailureNotifier = testFailureNotifier;
		this.distributedLatencyReporterFactory = distributedLatencyReporterFactory;
		this.executorService = executorServiceFactory().newFixedThreadPool(2,
				"remote-reporter");
		this.executorServiceFactory = new ExecutorServiceFactory();
	}

	public DistributedPerftenceApi reportingLatenciesTo(final URL reportsTo) {
		this.reportsTo = reportsTo;
		return this;
	}

	public DistributedPerftenceApi reportingThreads(final int threads) {
		this.executorService = executorServiceFactory()
				.newFixedThreadPool(threads, "remote-reporter");
		return this;
	}

	private ExecutorServiceFactory executorServiceFactory() {
		return this.executorServiceFactory;
	}

	public DistributedPerformanceTest test(final String id) {
		final RemoteLatencyReporter remoteReporter = reporterFactory()
				.forRemoteReporting(id, reportsTo());
		reporters().put(id, remoteReporter);
		return new DistributedPerformanceTest(id, this.testFailureNotifier,
				this.executorService, new RunNotifier() {

					@Override
					public boolean isFinished(final String id) {
						return defaultRunNotifier().isFinished(id);
					}

					@Override
					public void finished(String id) {
						defaultRunNotifier().finished(id);
						reporterFinished(id);
						shutdownExecutorService();

					}
				}, remoteReporter);
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

	private void shutdownExecutorService() {
		this.executorService.shutdown();
		LOG.info("Executor service for remote reporters has been shut down.");
	}

	private void reporterFinished(final String id) {
		final RemoteLatencyReporter reporter = reporters().get(id);
		if (reporter != null) {
			reporter.finished(id);
		}
	}

	private DefaultRunNotifier defaultRunNotifier() {
		return this.defaultRunNotifier;
	}

}