package org.fluentjava.perftence.reporting.summary;

public interface AdjustedField<VALUE> {

    public String name();

    public VALUE value();

    AdjustedField<String> asFormatted();
}
