package net.sf.perftence.reporting.summary;

public interface SummaryField<VALUE> {

    String name();

    VALUE value();
}
