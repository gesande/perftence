package net.sf.perftence.reporting;

public interface InvocationReporter {

    void latency(final int latency);

    void summary(final String id, final long elapsedTime,
            final long sampleCount, final long startTime);

    void throughput(final long currentDuration, final double throughput);

    boolean includeInvocationGraph();

    void invocationFailed(final Throwable t);

}
