package net.sf.perftence.reporting.summary;

public final class FieldAdjuster {

    @SuppressWarnings("static-method")
    public String adjust(final String field) {
        return String.format("%1$-25s", field);
    }
}
