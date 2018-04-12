package org.fluentjava.perftence.reporting.summary;

public interface SummaryField<VALUE> {

    String name();

    VALUE value();
}
