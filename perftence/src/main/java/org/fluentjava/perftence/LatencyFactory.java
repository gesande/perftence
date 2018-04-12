package org.fluentjava.perftence;

public final class LatencyFactory {

    /**
     * Calculates a latency for some operation.
     * 
     * @param callStart
     *            When an operation was started (in nano time), use
     *            System.nanoTime()
     * @return latency in milliseconds
     */

    @SuppressWarnings("static-method")
    public int newLatency(final long callStart) {
        return (int) ((System.nanoTime() - callStart) / 1000000);
    }
}
