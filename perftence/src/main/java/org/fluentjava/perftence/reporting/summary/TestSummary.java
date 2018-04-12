package org.fluentjava.perftence.reporting.summary;

public interface TestSummary {
    TestSummary field(final SummaryField<?> field);

    TestSummary endOfLine();

    TestSummary text(final String text);
}