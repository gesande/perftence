package org.fluentjava.perftence.reporting;

public interface FailureReporter {
    void invocationFailed(final Throwable t);

}
