package org.fluentjava.perftence.agents;

import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;

public interface ReporterFactoryForCategorySpecificLatencies {

    TestRuntimeReporter newReporter(final LatencyProvider latencyProvider, final int threads);

}
