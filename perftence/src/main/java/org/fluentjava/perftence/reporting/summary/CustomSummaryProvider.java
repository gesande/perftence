package org.fluentjava.perftence.reporting.summary;

public interface CustomSummaryProvider<SUMMARY> {
    void provideSummary(final Summary<SUMMARY> summary);
}
