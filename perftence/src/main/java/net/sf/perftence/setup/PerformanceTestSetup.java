package net.sf.perftence.setup;

import java.util.Collection;

import net.sf.perftence.graph.GraphWriter;
import net.sf.perftence.reporting.summary.SummaryAppender;

public interface PerformanceTestSetup {
    int threads();

    int duration();

    int invocations();

    int invocationRange();

    int throughputRange();

    Collection<SummaryAppender> summaryAppenders();

    Collection<GraphWriter> graphWriters();

}