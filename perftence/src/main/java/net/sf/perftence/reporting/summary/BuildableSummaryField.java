package net.sf.perftence.reporting.summary;

public interface BuildableSummaryField<T> {
    SummaryField<T> build();

    SummaryField<String> asFormatted();

}
