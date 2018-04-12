package org.fluentjava.perftence.reporting.summary;

public interface StatisticsSummaryProvider<SUMMARY> {

    /**
     * 
     * @param summary
     */
    void provideSummary(final SUMMARY summary);

    /**
     * Elapsed time for the test. Can be used when calculating throughput for the
     * summary
     * 
     * @param elapsedTime
     */
    StatisticsSummaryProvider<SUMMARY> elapsedTime(final long elapsedTime);

    /**
     * Total invocations for the test. Can be used when calculating throughput for
     * the summary
     * 
     * @param invocationCount
     */
    StatisticsSummaryProvider<SUMMARY> invocations(final long invocationCount);

}
