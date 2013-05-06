package net.sf.perftence.common;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DefaultTestRuntimeReporterFactory implements
        TestRuntimeReporterFactory {

    @Override
    public TestRuntimeReporter newRuntimeReporter(
            LatencyProvider latencyProvider, boolean includeInvocationGraph,
            PerformanceTestSetup setup, FailedInvocations failedInvocations) {
        return DefaultDependencyFactory.newRuntimeReporter(latencyProvider,
                includeInvocationGraph, setup, failedInvocations);
    }

}
