package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;

public interface InvocationReporterFactoryForCategorySpecificLatencies {

    TestRuntimeReporter newInvocationReporter(
            final LatencyProvider latencyProvider, final int threads);

}
