package net.sf.perftence.distributed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.sf.perftence.reporting.LatencyReporter;
import net.sf.perftence.reporting.TestRuntimeReporter;

final class DistributedTestRuntimeReporter implements TestRuntimeReporter {

	private final TestRuntimeReporter reporter;
	private final List<LatencyReporter> remoteReporters;
	private final ExecutorService executorService;

	public DistributedTestRuntimeReporter(
			final TestRuntimeReporter localReporter,
			final ExecutorService executorService,
			final LatencyReporter... remoteLatencyReporters) {
		this.reporter = localReporter;
		this.executorService = executorService;
		this.remoteReporters = new ArrayList<LatencyReporter>(
				Arrays.asList(remoteLatencyReporters));
	}

	@Override
	public void latency(final int latency) {
		reporter().latency(latency);
		this.executorService.execute(new Runnable() {
			@Override
			public void run() {
				reportLatencyRemotely(latency);
			}
		});
	}

	public void reportLatencyRemotely(final int latency) {
		for (final LatencyReporter reporter : remoteReporters()) {
			reporter.latency(latency);
		}
	}

	private List<LatencyReporter> remoteReporters() {
		return this.remoteReporters;
	}

	@Override
	public void throughput(final long currentDuration, final double throughput) {
		reporter().throughput(currentDuration, throughput);
	}

	private TestRuntimeReporter reporter() {
		return this.reporter;
	}

	@Override
	public void invocationFailed(Throwable t) {
		reporter().invocationFailed(t);
	}

	@Override
	public void summary(final String id, final long elapsedTime,
			final long sampleCount, final long startTime) {
		reporter().summary(id, elapsedTime, sampleCount, startTime);
	}

	@Override
	public boolean includeInvocationGraph() {
		return reporter().includeInvocationGraph();
	}

}