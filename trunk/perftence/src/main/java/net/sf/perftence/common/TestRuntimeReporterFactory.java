package net.sf.perftence.common;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.setup.PerformanceTestSetup;

public interface TestRuntimeReporterFactory {
    TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations);

    TestRuntimeReporter newRuntimeReporter(
            final LatencyProvider latencyProvider,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup setup,
            final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage,
            final ThroughputStorage throughputStorage);
}
