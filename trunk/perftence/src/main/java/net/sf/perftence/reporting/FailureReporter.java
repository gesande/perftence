package net.sf.perftence.reporting;

public interface FailureReporter {
    void invocationFailed(final Throwable t);

}
