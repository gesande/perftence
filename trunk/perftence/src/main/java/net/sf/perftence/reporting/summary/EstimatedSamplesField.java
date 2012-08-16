package net.sf.perftence.reporting.summary;

public final class EstimatedSamplesField extends SummaryFieldImpl<String> {

    private long sampleCount;
    private long estimatedInvocations;

    private EstimatedSamplesField(final FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    static EstimatedSamplesField create(final FieldAdjuster fieldAdjuster) {
        return new EstimatedSamplesField(SummaryFieldFactory.adjustedFieldName(
                Fields.Samples, fieldAdjuster));
    }

    public EstimatedSamplesField samplesSoFar(final long sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    private long sampleCount() {
        return this.sampleCount;
    }

    public SummaryField<String> estimatedSamples(final long estimatedInvocations) {
        this.estimatedInvocations = estimatedInvocations;
        return this;
    }

    private long estimatedInvocations() {
        return this.estimatedInvocations;
    }

    @Override
    public String value() {
        return new StringBuilder()
                .append(sampleCount())
                .append("/")
                .append(estimatedInvocations())
                .append(sampleCount() < estimatedInvocations() ? " (estimated)"
                        : "").toString();
    }
}