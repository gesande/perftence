package net.sf.perftence.reporting.summary;

public final class AdjustedFieldBuilderFactory {

    private FieldFormatter fieldFormatter;
    private FieldAdjuster fieldAdjuster;

    public AdjustedFieldBuilderFactory(final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        this.fieldFormatter = fieldFormatter;
        this.fieldAdjuster = fieldAdjuster;
    }

    public AdjustedFieldBuilder newInstance() {
        return new AdjustedFieldBuilder(fieldFormatter(), fieldAdjuster());
    }

    private FieldAdjuster fieldAdjuster() {
        return this.fieldAdjuster;
    }

    private FieldFormatter fieldFormatter() {
        return this.fieldFormatter;
    }
}
