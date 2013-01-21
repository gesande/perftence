package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.InvocationReporter;

public interface InvocationReporterFactoryForCategorySpecificLatencies {

    InvocationReporter newInvocationReporter(
            final LatencyProvider latencyProvider, final int threads);

}
