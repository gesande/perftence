package org.fluentjava.perftence.common;

import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;
import org.fluentjava.perftence.reporting.summary.FailedInvocations;
import org.fluentjava.perftence.setup.PerformanceTestSetup;

public interface TestRuntimeReporterFactory {
    TestRuntimeReporter newRuntimeReporter(final LatencyProvider latencyProvider, final boolean includeInvocationGraph,
            final PerformanceTestSetup setup, final FailedInvocations failedInvocations);

    TestRuntimeReporter newRuntimeReporter(final LatencyProvider latencyProvider, final boolean includeInvocationGraph,
            final PerformanceTestSetup setup, final FailedInvocations failedInvocations,
            final InvocationStorage invocationStorage, final ThroughputStorage throughputStorage);
}
