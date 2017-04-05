package net.sf.perftence.reporting.summary;

import net.sf.perftence.formatting.FieldFormatter;

public final class SummaryFieldBuilderForSingleValue<T>
		implements SummaryFieldBuilder<T>, BuildableSummaryField<T> {

	private final FieldFormatter fieldFormatter;
	private final FieldAdjuster fieldAdjuster;

	private T value;
	private FieldDefinition fieldDefinition = noName();

	SummaryFieldBuilderForSingleValue(final FieldFormatter fieldFormatter,
			final FieldAdjuster fieldAdjuster) {
		this.fieldFormatter = fieldFormatter;
		this.fieldAdjuster = fieldAdjuster;
	}

	SummaryFieldBuilderForSingleValue<T> field(
			final FieldDefinition fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
		return this;
	}

	private static FieldDefinition noName() {
		return new FieldDefinition() {

			@Override
			public String fullName() {
				return "<no name>";
			}
		};
	}

	@Override
	public BuildableSummaryField<T> value(final T value) {
		this.value = value;
		return this;
	}

	private FieldDefinition fieldDefinition() {
		return this.fieldDefinition;
	}

	private T value() {
		return this.value;
	}

	@Override
	public SummaryField<T> build() {
		return new SummaryFieldImpl<>(adjustedFieldName(), value());
	}

	@Override
	public SummaryField<String> asFormatted() {
		return new SummaryFieldImpl<>(adjustedFieldName(),
				value() == null ? "<value was null>" : format(value()));
	}

	private FieldDefinition adjustedFieldName() {
		return adjustedFieldName(fieldDefinition());
	}

	private String format(final Object value) {
		return fieldFormatter().format(value);
	}

	private FieldFormatter fieldFormatter() {
		return this.fieldFormatter;
	}

	private FieldDefinition adjustedFieldName(final FieldDefinition def) {
		return new FieldDefinition() {
			@Override
			public String fullName() {
				return adjust(def.fullName());
			}
		};
	}

	private String adjust(final String field) {
		return this.fieldAdjuster.adjust(field);
	}

}