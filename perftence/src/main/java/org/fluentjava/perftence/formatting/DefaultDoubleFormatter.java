package org.fluentjava.perftence.formatting;

import java.text.DecimalFormat;

public final class DefaultDoubleFormatter {
    private final static DecimalFormat DF = new DecimalFormat("####.###");

    @SuppressWarnings("static-method")
    public String format(final double value) {
        return DF.format(value);
    }
}
