package net.sf.perftence.reporting.summary;

public interface StatisticsSummaryProvider<SUMMARY> {

    /**
     * 
     * @param summary
     */
    void provideSummary(SUMMARY summary);

    /**
     * Elapsed time for the test. Can be used when calculating throughput for
     * the summary
     * 
     * @param elapsedTime
     */
    StatisticsSummaryProvider<SUMMARY> elapsedTime(long elapsedTime);

    /**
     * Total invocations for the test. Can be used when calculating throughput
     * for the summary
     * 
     * @param invocationCount
     */
    StatisticsSummaryProvider<SUMMARY> invocations(long invocationCount);

}
