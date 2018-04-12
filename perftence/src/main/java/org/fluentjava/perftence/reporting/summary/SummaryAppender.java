package org.fluentjava.perftence.reporting.summary;

public interface SummaryAppender {

    void append(final Summary<?> summary);
}
