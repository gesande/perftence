package net.sf.perftence.reporting.summary;

public final class SamplesField extends SummaryFieldImpl<String> {

	private long samplesSoFar;
	private long samplesTotal;

	public SamplesField(final FieldDefinition fieldDefinition) {
		super(fieldDefinition);
	}

	static SamplesField create(final FieldAdjuster fieldAdjuster) {
		return new SamplesField(SummaryFieldFactory.adjustedFieldName(
				Fields.Samples, fieldAdjuster));
	}

	public SamplesField samplesSoFar(final long samplesSoFar) {
		this.samplesSoFar = samplesSoFar;
		return this;
	}

	public SamplesField samplesTotal(final long samplesTotal) {
		this.samplesTotal = samplesTotal;
		return this;
	}

	@Override
	public String value() {
		return new StringBuilder().append(samplesSoFar()).append("/")
				.append(samplesTotal()).toString();
	}

	private long samplesTotal() {
		return this.samplesTotal;
	}

	private long samplesSoFar() {
		return this.samplesSoFar;
	}
}