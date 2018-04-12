package org.fluentjava.perftence.reporting.summary;

public interface IntermediateSummary {

    IntermediateSummary endOfLine();

    IntermediateSummary text(final String string);

    IntermediateSummary field(final AdjustedField<?> field);

}
