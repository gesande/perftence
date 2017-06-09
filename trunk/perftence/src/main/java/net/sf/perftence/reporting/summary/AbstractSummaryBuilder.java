package net.sf.perftence.reporting.summary;

public abstract class AbstractSummaryBuilder implements TestSummaryBuilder {

    protected AbstractSummaryBuilder() {
    }

    @Override
    public String build() {
        final StringBuilder sb = new StringBuilder();
        fields(newSummary(sb));
        return sb.toString();
    }

    private static TestSummary newSummary(final StringBuilder sb) {
        return new TestSummary() {
            @Override
            public TestSummary field(final SummaryField<?> field) {
                sb.append(field.name()).append(field.value()).append(newLine());
                return this;
            }

            @Override
            public TestSummary endOfLine() {
                sb.append(newLine());
                return this;
            }

            @Override
            public TestSummary text(final String text) {
                sb.append(text);
                return this;
            }

            private String newLine() {
                return "\n";
            }
        };
    }

    protected abstract void fields(final TestSummary summary);

}
