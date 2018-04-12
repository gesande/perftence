package org.fluentjava.perftence.setup;

import java.util.Collection;

import org.fluentjava.perftence.graph.GraphWriter;
import org.fluentjava.perftence.reporting.summary.SummaryAppender;

public interface PerformanceTestSetup {
    int threads();

    int duration();

    int invocations();

    int invocationRange();

    int throughputRange();

    Collection<SummaryAppender> summaryAppenders();

    Collection<GraphWriter> graphWriters();

}