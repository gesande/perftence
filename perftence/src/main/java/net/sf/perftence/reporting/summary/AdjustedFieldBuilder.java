package net.sf.perftence.reporting.summary;

public final class AdjustedFieldBuilder {
    private FieldFormatter fieldFormatter;
    private FieldAdjuster fieldAdjuster;

    AdjustedFieldBuilder(final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        this.fieldFormatter = fieldFormatter;
        this.fieldAdjuster = fieldAdjuster;
    }

    public AdjustedField<Double> field(final String name, final double value) {
        return new AdjustedField<Double>() {

            @Override
            public String name() {
                return adjust(name);
            }

            @Override
            public Double value() {
                return value;
            }

            @Override
            public AdjustedField<String> asFormatted() {
                return new Formatted(this, fieldFormatter());
            }
        };
    }

    public AdjustedField<Long> field(final String name, final long value) {
        return new AdjustedField<Long>() {

            @Override
            public String name() {
                return adjust(name);
            }

            @Override
            public Long value() {
                return value;
            }

            @Override
            public AdjustedField<String> asFormatted() {
                return new Formatted(this, fieldFormatter());
            }
        };
    }

    public AdjustedField<Integer> field(final String name, final int value) {
        return new AdjustedField<Integer>() {

            @Override
            public String name() {
                return adjust(name);
            }

            @Override
            public Integer value() {
                return value;
            }

            @Override
            public AdjustedField<String> asFormatted() {
                return new Formatted(this, fieldFormatter());
            }
        };
    }

    private static class Formatted implements AdjustedField<String> {

        private final AdjustedField<?> field;
        private final FieldFormatter formatter;

        public Formatted(final AdjustedField<?> field,
                final FieldFormatter fieldFormatter) {
            this.field = field;
            this.formatter = fieldFormatter;
        }

        private AdjustedField<?> field() {
            return this.field;
        }

        @Override
        public String name() {
            return field().name();
        }

        @Override
        public String value() {
            return fieldFormatter().format(field().value());
        }

        private FieldFormatter fieldFormatter() {
            return this.formatter;
        }

        @Override
        public AdjustedField<String> asFormatted() {
            return this;
        }

    }

    private FieldFormatter fieldFormatter() {
        return this.fieldFormatter;
    }

    private String adjust(String name) {
        return fieldAdjuster().adjust(name);
    }

    private FieldAdjuster fieldAdjuster() {
        return this.fieldAdjuster;
    }
}
