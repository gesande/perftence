package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.perftence.PercentileRequirement;
import net.sf.perftence.PerformanceRequirements;

public final class PerformanceRequirementsPojo implements
        PerformanceRequirements {

    public static class PerformanceRequirementsBuilder {
        private int average = -1;
        private int median = -1;
        private int max = -1;
        private long totalTime = -1;
        private int throughput = -1;
        private List<PercentileRequirement> percentiles;

        private PerformanceRequirementsBuilder() {
            this.percentiles = new ArrayList<PercentileRequirement>();
        }

        public PerformanceRequirementsBuilder max(final int max) {
            this.max = max;
            return this;
        }

        public PerformanceRequirementsBuilder median(final int median) {
            this.median = median;
            return this;
        }

        public PerformanceRequirementsBuilder average(final int average) {
            this.average = average;
            return this;
        }

        public PerformanceRequirementsBuilder totalTime(final int totalTime) {
            this.totalTime = totalTime;
            return this;
        }

        public PerformanceRequirementsBuilder throughput(final int throughput) {
            this.throughput = throughput;
            return this;
        }

        private List<PercentileRequirement> percentiles() {
            return this.percentiles;
        }

        public PerformanceRequirements build() {
            return newPerformanceRequirementsPojo(this.average, this.median,
                    this.max, this.totalTime, this.throughput,
                    percentilesToArray());
        }

        private PercentileRequirement[] percentilesToArray() {
            return percentiles().toArray(
                    new PercentileRequirement[percentiles().size()]);
        }

        private static PerformanceRequirements newPerformanceRequirementsPojo(
                final int average, final int median, final int max,
                final long totalTime, final int throughput,
                final PercentileRequirement[] percentiles) {
            return new PerformanceRequirementsPojo(average, median, max,
                    totalTime, throughput, percentiles);
        }

        public static PerformanceRequirements noRequirements() {
            return newPerformanceRequirementsPojo(-1, -1, -1, -1, -1,
                    new PercentileRequirement[0]);
        }

        public PerformanceRequirementsBuilder percentile90(final int value) {
            return addPercentileRequirement(newPercentileRequirement(90, value));
        }

        private PerformanceRequirementsBuilder addPercentileRequirement(
                PercentileRequirement newPercentileRequirement) {
            this.percentiles.add(newPercentileRequirement);
            return this;
        }

        private static PercentileRequirement newPercentileRequirement(
                final int percentage, final int value) {
            return new PercentileRequirementPojo(percentage, value);
        }

        public PerformanceRequirementsBuilder percentile95(final int value) {
            return addPercentileRequirement(newPercentileRequirement(95, value));
        }

        public PerformanceRequirementsBuilder percentile99(final int value) {
            return addPercentileRequirement(newPercentileRequirement(99, value));
        }
    }

    private final int average;
    private final int median;
    private final int max;
    private final long totalTime;
    private final int throughput;
    private PercentileRequirement[] percentileRequirements;

    private PerformanceRequirementsPojo(final int average, final int median,
            final int max, final long totalTime, final int throughput,
            final PercentileRequirement[] percentileRequirements) {
        this.average = average;
        this.median = median;
        this.max = max;
        this.totalTime = totalTime;
        this.throughput = throughput;
        this.percentileRequirements = percentileRequirements;
    }

    public static PerformanceRequirements noRequirements() {
        return builder().build();
    }

    public static PerformanceRequirementsBuilder builder() {
        return new PerformanceRequirementsBuilder();
    }

    @Override
    public int average() {
        return this.average;
    }

    @Override
    public int median() {
        return this.median;
    }

    @Override
    public int max() {
        return this.max;
    }

    @Override
    public long totalTime() {
        return this.totalTime;
    }

    @Override
    public int throughput() {
        return this.throughput;
    }

    @Override
    public PercentileRequirement[] percentileRequirements() {
        return Arrays.copyOf(this.percentileRequirements,
                this.percentileRequirements.length);
    }

    @Override
    public String toString() {
        return "PerformanceRequirementsPojo [average=" + average()
                + ", median=" + median() + ", max=" + max() + ", totalTime="
                + totalTime() + ", throughput=" + throughput()
                + ", percentileRequirements="
                + Arrays.toString(percentileRequirements()) + "]";
    }
}