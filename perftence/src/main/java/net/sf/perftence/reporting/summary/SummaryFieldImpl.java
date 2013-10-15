package net.sf.perftence.reporting.summary;

class SummaryFieldImpl<T> implements SummaryField<T> {
	private final FieldDefinition fieldDefinition;
	private T value;

	SummaryFieldImpl(final FieldDefinition fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	SummaryFieldImpl(final FieldDefinition fieldDefinition, final T value) {
		this.fieldDefinition = fieldDefinition;
		this.value = value;
	}

	@Override
	public T value() {
		return this.value;
	}

	@Override
	public String name() {
		return this.fieldDefinition.fullName();
	}

}