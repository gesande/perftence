package org.fluentjava.perftence.fluent;

import org.fluentjava.perftence.PercentileRequirement;

final class PercentileRequirementPojo implements PercentileRequirement {

    private final int percentage;
    private final int millis;

    public PercentileRequirementPojo(final int percentage, final int millis) {
        this.percentage = percentage;
        this.millis = millis;
    }

    @Override
    public int percentage() {
        return this.percentage;
    }

    @Override
    public int millis() {
        return this.millis;
    }

    @Override
    public String toString() {
        return "PercentileRequirementPojo [percentage()=" + percentage() + ", millis()=" + millis() + "]";
    }

}
