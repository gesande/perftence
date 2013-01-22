package net.sf.perftence.reporting;

public interface TestRuntimeReporter extends LatencyReporter,
        ThroughputReporter, FailureReporter, SummaryReporter {

    boolean includeInvocationGraph();

}
