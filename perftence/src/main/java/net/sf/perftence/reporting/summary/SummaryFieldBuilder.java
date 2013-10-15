package net.sf.perftence.reporting.summary;

public interface SummaryFieldBuilder<T> {

	BuildableSummaryField<T> value(final T value);

}
