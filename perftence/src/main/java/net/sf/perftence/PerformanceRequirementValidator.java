package net.sf.perftence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PerformanceRequirementValidator {

    private final static Logger LOG = LoggerFactory
            .getLogger(PerformanceRequirementValidator.class);

    private final PerfTestFailureFactory perfTestFailureFactory;
    private final PerformanceRequirements requirements;
    private final StatisticsProvider statisticsProvider;

    public PerformanceRequirementValidator(
            final PerformanceRequirements requirements,
            final StatisticsProvider statisticsProvider,
            final PerfTestFailureFactory perfTestFailureFactory) {
        this.requirements = requirements;
        this.statisticsProvider = statisticsProvider;
        this.perfTestFailureFactory = perfTestFailureFactory;
    }

    private StatisticsProvider statistics() {
        return this.statisticsProvider;
    }

    private PerformanceRequirements requirements() {
        return this.requirements;
    }

    private boolean hasRequirements() {
        return requirements() != null;
    }

    public void checkRuntimeLatency(final String id, final int latency) {
        if (hasRequirements() && requirements().max() >= 0
                && latency > requirements().max()) {
            throw newPerfTestFailure("Method " + id
                    + " exceeded time limit of " + requirements().max()
                    + " ms running " + latency + " ms");
        }
    }

    public void checkAllRequirements(final String id, final long elapsedTime) {
        final long requiredMax = requirements().max();
        if (requiredMax >= 0) {
            final long maxLatency = statistics().maxLatency();
            if (maxLatency > requiredMax) {
                throw newPerfTestFailure("Test " + id
                        + " exceeded required maximum latency of "
                        + requiredMax + " ms. Measured: " + maxLatency + " ms");
            }
            log().info("Test '{}' passed maximum latency check.", id);
        }

        final long requiredTotalTime = requirements().totalTime();
        if (requiredTotalTime >= 0) {
            if (elapsedTime > requiredTotalTime) {
                throw newPerfTestFailure("Test " + id
                        + " exceeded elapsed time of " + requiredTotalTime
                        + " ms. Elapsed time was " + elapsedTime + " ms");
            }
            log().info("Test '{}' passed total elapsed time check.", id);
        }
        final int requiredThroughput = requirements().throughput();
        if (requiredThroughput > 0) {
            final long actualThroughput = statistics().sampleCount() * 1000
                    / elapsedTime;
            if (actualThroughput < requiredThroughput) {
                throw newPerfTestFailure("Test " + id
                        + " had a throughput of only " + actualThroughput
                        + " calls per second, required: " + requiredThroughput
                        + " calls per second");
            }
            log().info("Test '{}' passed throughput check.", id);
        }
        final int requiredAverage = requirements().average();
        if (requiredAverage >= 0) {
            if (statistics().averageLatency() > requiredAverage) {
                throw newPerfTestFailure("Average execution time of " + id
                        + " exceeded the requirement of " + requiredAverage
                        + " ms, measured " + statistics().averageLatency()
                        + " ms");
            }
            log().info("Test '{}' passed average latency check.", id);
        }
        final int requiredMedian = requirements().median();
        if (requiredMedian >= 0) {
            if (statistics().median() > requiredMedian) {
                throw newPerfTestFailure("Median latency for " + id
                        + " exceeded the requirement of " + requiredMedian
                        + " ms, measured " + statistics().median() + " ms");
            }
            log().info("Test '{}' passed median latency check.", id);
        }

        for (final PercentileRequirement percentile : requirements()
                .percentileRequirements()) {
            final long measuredLatency = statistics().percentileLatency(
                    percentile.percentage());
            if (measuredLatency > percentile.millis()) {
                throw newPerfTestFailure(percentile.percentage()
                        + "-percentile of " + id
                        + " exceeded the requirement of " + percentile.millis()
                        + " ms, measured " + measuredLatency + " ms");
            }
            log().info("Test '{}' passed {} percentile check.", id,
                    percentile.percentage());
        }
    }

    private static Logger log() {
        return LOG;
    }

    private RuntimeException newPerfTestFailure(final String message) {
        return perfTestFailureFactory().newPerfTestFailure(message);
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

}