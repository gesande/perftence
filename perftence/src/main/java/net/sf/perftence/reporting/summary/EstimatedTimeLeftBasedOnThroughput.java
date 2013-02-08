package net.sf.perftence.reporting.summary;

import net.sf.perftence.formatting.FieldFormatter;

public final class EstimatedTimeLeftBasedOnThroughput extends
        SummaryFieldImpl<String> {
    private final FieldFormatter fieldFormatter;

    private long invocationsLeft;
    private double currentThroughput;

    private EstimatedTimeLeftBasedOnThroughput(
            final FieldFormatter fieldFormatter,
            final FieldDefinition fieldDefinition) {
        super(fieldDefinition);
        this.fieldFormatter = fieldFormatter;
    }

    static EstimatedTimeLeftBasedOnThroughput create(
            FieldFormatter fieldFormatter, FieldAdjuster fieldAdjuster) {
        return new EstimatedTimeLeftBasedOnThroughput(fieldFormatter,
                SummaryFieldFactory.adjustedFieldName(Fields.EstimatedTimeLeft,
                        fieldAdjuster));
    }

    public EstimatedTimeLeftBasedOnThroughput invocationsLeft(
            final long invocationsLeft) {
        this.invocationsLeft = invocationsLeft;
        return this;
    }

    public SummaryField<String> throughput(final double currentThroughput) {
        this.currentThroughput = currentThroughput;
        return this;
    }

    private FieldFormatter fieldFormatter() {
        return this.fieldFormatter;
    }

    @Override
    public String value() {
        return new StringBuilder().append(format(estimatedTimeLeft()))
                .append(" (sec)").toString();
    }

    private String format(final Double value) {
        return fieldFormatter().format(value);
    }

    private double estimatedTimeLeft() {
        return invocationsLeft() / currentThroughput();
    }

    private double currentThroughput() {
        return this.currentThroughput;
    }

    private long invocationsLeft() {
        return this.invocationsLeft;
    }
}