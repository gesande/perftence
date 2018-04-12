package org.fluentjava.perftence.reporting.summary;

public interface SummaryFieldBuilder<T> {

    BuildableSummaryField<T> value(final T value);

}
