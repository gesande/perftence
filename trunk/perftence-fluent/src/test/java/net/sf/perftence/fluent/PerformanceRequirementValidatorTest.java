package net.sf.perftence.fluent;

import net.sf.perftence.PerfTestFailure;
import net.sf.perftence.PerfTestFailureFactory;
import net.sf.perftence.PerformanceRequirements;
import net.sf.perftence.StatisticsProvider;

import org.junit.Test;

public class PerformanceRequirementValidatorTest {

    @SuppressWarnings("static-method")
    @Test
    public void noRequirements() {
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 0;
            }

            @Override
            public long maxLatency() {
                return 0;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 0;
            }
        };
        final PerformanceRequirementValidator validator = new PerformanceRequirementValidator(
                PerformanceRequirementsPojo.noRequirements(),
                statisticsProvider, new PerfTestFailureFactory());
        validator.checkRuntimeLatency("maxLatencyFailed", 0);
        validator.checkAllRequirements("maxLatencyFailed", 0);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void maxLatencyFailed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().max(50).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 0;
            }

            @Override
            public long maxLatency() {
                return 100;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 0;
            }
        };
        newValidator(requirements, statisticsProvider).checkRuntimeLatency(
                "maxLatencyFailed", 500);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void averageFailed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().average(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 0;
            }

            @Override
            public long maxLatency() {
                return 100;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "averageFailed", 1000);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void throughputFailed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().throughput(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 999;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 0;
            }

            @Override
            public long maxLatency() {
                return 100;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "throughputFailed", 10000);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void totalTimeFailed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().totalTime(10000).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 0;
            }

            @Override
            public long maxLatency() {
                return 100;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "totalTimeFailed", 10001);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void medianFailed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().median(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 101;
            }

            @Override
            public long maxLatency() {
                return 150;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 0;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "medianFailed", 10001);
    }

    @SuppressWarnings("static-method")
    @Test
    public void totalTimePassed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().totalTime(10001).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 0;
            }

            @Override
            public long sampleCount() {
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 101;
            }

            @Override
            public long maxLatency() {
                return 150;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 10001;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "totalTimePassed", 10001);
    }

    @SuppressWarnings("static-method")
    @Test
    public void throughputPassed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().throughput(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 100;
            }

            @Override
            public long sampleCount() {
                return 1001;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 101;
            }

            @Override
            public long maxLatency() {
                return 150;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 10001;
            }

            @Override
            public double averageLatency() {
                return 101;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "throughputPassed", 10001);
    }

    @SuppressWarnings("static-method")
    @Test
    public void averagePassed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().average(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 100;
            }

            @Override
            public long sampleCount() {
                return 1001;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 101;
            }

            @Override
            public long maxLatency() {
                return 150;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 10001;
            }

            @Override
            public double averageLatency() {
                return 100;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "averagePassed", 10001);
    }

    @SuppressWarnings("static-method")
    @Test
    public void medianPassed() {
        final PerformanceRequirements requirements = PerformanceRequirementsPojo
                .builder().median(100).build();
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                return 100;
            }

            @Override
            public long sampleCount() {
                return 1001;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 0;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 100;
            }

            @Override
            public long maxLatency() {
                return 150;
            }

            @Override
            public boolean hasSamples() {
                return false;
            }

            @Override
            public long duration() {
                return 10001;
            }

            @Override
            public double averageLatency() {
                return 100;
            }
        };
        newValidator(requirements, statisticsProvider).checkAllRequirements(
                "medianPassed", 10001);
    }

    private static PerformanceRequirementValidator newValidator(
            PerformanceRequirements requirements,
            StatisticsProvider statisticsProvider) {
        return new PerformanceRequirementValidator(requirements,
                statisticsProvider, new PerfTestFailureFactory());
    }
}
