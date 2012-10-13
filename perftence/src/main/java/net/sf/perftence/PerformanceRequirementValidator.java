package net.sf.perftence;

public final class PerformanceRequirementValidator {

    private PerfTestFailureFactory perfTestFailureFactory;
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

    public void checkMaxLatency(final String id, final int latency) {
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
                throw newPerfTestFailure("The maximum latency of "
                        + requiredMax + " ms was exceeded, Measured: "
                        + maxLatency + " ms");
            }
        }
        final long requiredTotalTime = requirements().totalTime();
        if (requiredTotalTime >= 0 && elapsedTime > requiredTotalTime) {
            throw newPerfTestFailure("Test run " + id + " exceeded timeout of "
                    + requiredTotalTime + " ms running " + elapsedTime + " ms");

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
        }
        final int requiredAverage = requirements().average();
        if (requiredAverage >= 0
                && statistics().averageLatency() > requiredAverage) {
            throw newPerfTestFailure("Average execution time of " + id
                    + " exceeded the requirement of " + requiredAverage
                    + " ms, measured " + statistics().averageLatency() + " ms");
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
        }
    }

    private RuntimeException newPerfTestFailure(final String message) {
        return perfTestFailureFactory().newPerfTestFailure(message);
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

}