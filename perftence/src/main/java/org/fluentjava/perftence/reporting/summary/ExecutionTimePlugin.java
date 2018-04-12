package org.fluentjava.perftence.reporting.summary;

public final class ExecutionTimePlugin implements SummaryFieldPlugin<Long> {
    private final SummaryFieldFactory summaryFieldFactory;
    private final FieldValueResolver<Long> valueResolver;

    ExecutionTimePlugin(final SummaryFieldFactory summaryFieldFactory, final FieldValueResolver<Long> valueResolver) {
        this.summaryFieldFactory = summaryFieldFactory;
        this.valueResolver = valueResolver;
    }

    @Override
    public BuildableSummaryField<Long> field() {
        return summaryFieldFactory().executionTime().value(valueResolver().value());
    }

    private FieldValueResolver<Long> valueResolver() {
        return this.valueResolver;
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }
}
