package org.fluentjava.perftence.reporting.summary;

public interface BuildableSummaryField<T> {
    SummaryField<T> build();

    SummaryField<String> asFormatted();

}
