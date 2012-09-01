package net.sf.perftence;

public final class PercentileRequirement {
    private final int percentage;
    private final int millis;

    public PercentileRequirement(final int percentage, final int millis) {
        this.percentage = percentage;
        this.millis = millis;
    }

    public int percentage() {
        return this.percentage;
    }

    public int millis() {
        return this.millis;
    }

}
