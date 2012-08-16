package net.sf.perftence.reporting;

import java.text.DecimalFormat;

public class DefaultDoubleFormatter {
    private final static DecimalFormat DF = new DecimalFormat("####.###");

    @SuppressWarnings("static-method")
    public String format(final double value) {
        return DF.format(value);
    }
}
