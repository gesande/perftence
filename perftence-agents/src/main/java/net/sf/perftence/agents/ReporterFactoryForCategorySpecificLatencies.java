package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;

public interface ReporterFactoryForCategorySpecificLatencies {

    TestRuntimeReporter newReporter(final LatencyProvider latencyProvider, final int threads);

}
