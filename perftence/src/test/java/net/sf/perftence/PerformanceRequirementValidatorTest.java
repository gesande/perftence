package net.sf.perftence;

import net.sf.perftence.fluent.PerformanceRequirementsPojo;

import org.junit.Test;

public class PerformanceRequirementValidatorTest {

    @SuppressWarnings("static-method")
    @Test
    public void noRequirements() {
        final StatisticsProvider statisticsProvider = new StatisticsProvider() {

            @Override
            public double throughput() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long sampleCount() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long minLatency() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long median() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long maxLatency() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean hasSamples() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public long duration() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public double averageLatency() {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long sampleCount() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long percentileLatency(int percentile) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long minLatency() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long median() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long maxLatency() {
                // TODO Auto-generated method stub
                return 100;
            }

            @Override
            public boolean hasSamples() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public long duration() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public double averageLatency() {
                // TODO Auto-generated method stub
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

    private static PerformanceRequirementValidator newValidator(
            PerformanceRequirements requirements,
            StatisticsProvider statisticsProvider) {
        return new PerformanceRequirementValidator(requirements,
                statisticsProvider, new PerfTestFailureFactory());
    }
}
