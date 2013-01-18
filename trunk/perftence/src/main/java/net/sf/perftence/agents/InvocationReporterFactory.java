package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.InvocationReporter;

public interface InvocationReporterFactory {

    InvocationReporter newInvocationReporter(
            final LatencyProvider latencyProvider, final int threads);

}
