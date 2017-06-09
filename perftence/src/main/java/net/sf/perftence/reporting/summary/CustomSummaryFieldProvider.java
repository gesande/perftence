package net.sf.perftence.reporting.summary;

public interface CustomSummaryFieldProvider {
    public <VALUE> SummaryFieldBuilder<VALUE> custom(final FieldDefinition field, final Class<VALUE> valueType);
}
