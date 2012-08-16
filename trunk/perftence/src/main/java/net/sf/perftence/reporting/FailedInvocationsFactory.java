package net.sf.perftence.reporting;

import net.sf.perftence.reporting.summary.AdjustedFieldBuilder;

public final class FailedInvocationsFactory {

    private final DefaultDoubleFormatter defaultDoubleFormatter;
    private final AdjustedFieldBuilder adjustedFieldBuilder;

    public FailedInvocationsFactory(
            final DefaultDoubleFormatter defaultDoubleFormatter,
            final AdjustedFieldBuilder adjustedFieldBuilder) {
        this.defaultDoubleFormatter = defaultDoubleFormatter;
        this.adjustedFieldBuilder = adjustedFieldBuilder;
    }

    public FailedInvocations newInstance() {
        return new FailedInvocations(fieldBuilder(), formatter());
    }

    private DefaultDoubleFormatter formatter() {
        return this.defaultDoubleFormatter;
    }

    private AdjustedFieldBuilder fieldBuilder() {
        return this.adjustedFieldBuilder;
    }

}
