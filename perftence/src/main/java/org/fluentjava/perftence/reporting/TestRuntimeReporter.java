package org.fluentjava.perftence.reporting;

public interface TestRuntimeReporter extends LatencyReporter, ThroughputReporter, FailureReporter, SummaryReporter {

    boolean includeInvocationGraph();

}
