package org.fluentjava.perftence.reporting.summary;

import org.fluentjava.perftence.formatting.FieldFormatter;

public class SummaryFieldFactory implements CustomSummaryFieldProvider {

    private final FieldFormatter fieldFormatter;
    private final FieldAdjuster fieldAdjuster;

    public static SummaryFieldFactory create(final FieldFormatter fieldFormatter, final FieldAdjuster fieldAdjuster) {
        return new SummaryFieldFactory(fieldFormatter, fieldAdjuster);
    }

    private SummaryFieldFactory(final FieldFormatter fieldFormatter, final FieldAdjuster fieldAdjuster) {
        this.fieldFormatter = fieldFormatter;
        this.fieldAdjuster = fieldAdjuster;
    }

    private FieldFormatter fieldFormatter() {
        return this.fieldFormatter;
    }

    private FieldAdjuster fieldAdjuster() {
        return this.fieldAdjuster;
    }

    static FieldDefinition adjustedFieldName(final FieldDefinition def, final FieldAdjuster fieldAdjuster) {
        return new FieldDefinition() {
            @Override
            public String fullName() {
                return fieldAdjuster.adjust(def.fullName());
            }
        };
    }

    public SummaryFieldBuilder<Long> max() {
        return new SummaryFieldBuilderForSingleValue<Long>(fieldFormatter(), fieldAdjuster()).field(Fields.Max);
    }

    public SummaryFieldBuilder<Integer> threads() {
        return new SummaryFieldBuilderForSingleValue<Integer>(fieldFormatter(), fieldAdjuster()).field(Fields.Threads);
    }

    public SummaryFieldBuilder<Long> executionTime() {
        return new SummaryFieldBuilderForSingleValue<Long>(fieldFormatter(), fieldAdjuster())
                .field(Fields.ExecutionTime);
    }

    public SummaryFieldBuilder<Double> throughput() {
        return new SummaryFieldBuilderForSingleValue<Double>(fieldFormatter(), fieldAdjuster())
                .field(Fields.Throughput);
    }

    public SummaryFieldBuilder<Long> percentile95() {
        return new SummaryFieldBuilderForSingleValue<Long>(fieldFormatter(), fieldAdjuster())
                .field(Fields.Percentile95);
    }

    public SummaryFieldBuilder<Long> median() {
        return new SummaryFieldBuilderForSingleValue<Long>(fieldFormatter(), fieldAdjuster()).field(Fields.Median);
    }

    public SummaryFieldBuilder<Double> average() {
        return new SummaryFieldBuilderForSingleValue<Double>(fieldFormatter(), fieldAdjuster()).field(Fields.Average);
    }

    public EstimatedTimeLeftBasedOnDuration estimatedTimeLeftBasedOnDuration() {
        return EstimatedTimeLeftBasedOnDuration.create(fieldFormatter(), fieldAdjuster());
    }

    public static class EstimatedTimeLeftBasedOnDuration extends SummaryFieldImpl<String> {
        private final FieldFormatter fieldFormatter;

        private long actualTimeLeft;
        private double estimatedTimeLeft;

        private EstimatedTimeLeftBasedOnDuration(final FieldFormatter fieldFormatter,
                final FieldDefinition fieldDefinition) {
            super(fieldDefinition);
            this.fieldFormatter = fieldFormatter;
        }

        public static EstimatedTimeLeftBasedOnDuration create(final FieldFormatter fieldFormatter,
                final FieldAdjuster fieldAdjuster) {
            return new EstimatedTimeLeftBasedOnDuration(fieldFormatter,
                    adjustedFieldName(Fields.EstimatedTimeLeft, fieldAdjuster));
        }

        public EstimatedTimeLeftBasedOnDuration actualTimeLeft(final long actualTimeLeft) {
            this.actualTimeLeft = actualTimeLeft;
            return this;
        }

        public SummaryField<String> estimatedTimeLeft(final double estimatedTimeLeft) {
            this.estimatedTimeLeft = estimatedTimeLeft;
            return this;
        }

        @Override
        public String value() {
            return new StringBuilder().append(actualTimeLeft() <= 0 ? "less than " : "")
                    .append(format(estimatedTimeLeft())).append(" (sec)").toString();
        }

        private String format(final Double value) {
            return fieldFormatter().format(value);
        }

        private FieldFormatter fieldFormatter() {
            return this.fieldFormatter;
        }

        private double estimatedTimeLeft() {
            return this.estimatedTimeLeft;
        }

        private long actualTimeLeft() {
            return this.actualTimeLeft;
        }
    }

    @Override
    public <VALUE> SummaryFieldBuilder<VALUE> custom(final FieldDefinition field, final Class<VALUE> valueType) {
        return new SummaryFieldBuilderForSingleValue<VALUE>(fieldFormatter(), fieldAdjuster()).field(field);
    }

    public SamplesField samples() {
        return SamplesField.create(fieldAdjuster());
    }

    public EstimatedSamplesField estimatedSamples() {
        return EstimatedSamplesField.create(fieldAdjuster());
    }

    public EstimatedTimeLeftBasedOnThroughput estimatedTimeLeftBasedOnThroughput() {
        return EstimatedTimeLeftBasedOnThroughput.create(fieldFormatter(), fieldAdjuster());
    }
}