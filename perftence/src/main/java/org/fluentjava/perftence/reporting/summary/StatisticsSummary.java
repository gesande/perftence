package org.fluentjava.perftence.reporting.summary;

public interface StatisticsSummary<SUMMARY> {

    public SUMMARY throughput(final double throughput);

    public SUMMARY minResponseTime(final long value, final String unit);

    public SUMMARY maxResponseTime(final long value, final String unit);

    public SUMMARY averageResponseTime(final double mean, final String unit);

    public SUMMARY median(final long median, final String unit);

    public SUMMARY standardDeviation(final double standardDeviation);

    public SUMMARY variance(double variance);

    public SUMMARY endOfLine();

    public SUMMARY percentileHeader();

    public SUMMARY note(final String text);

    public SUMMARY percentile90(final long percentileValue);

    public SUMMARY percentile95(final long percentileValue);

    public SUMMARY percentile96(final long percentileValue);

    public SUMMARY percentile97(final long percentileValue);

    public SUMMARY percentile98(final long percentileValue);

    public SUMMARY percentile99(final long percentileValue);

}
