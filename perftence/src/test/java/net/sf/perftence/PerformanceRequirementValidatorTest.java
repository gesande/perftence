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
        validator.checkRuntimeLatency("id", 0);
        validator.checkAllRequirements("id", 0);
    }

    @SuppressWarnings("static-method")
    @Test(expected = PerfTestFailure.class)
    public void maxLatency() {
        final int max = 50;
        PerformanceRequirements requirements = new PerformanceRequirements() {

            @Override
            public long totalTime() {
                return 1000;
            }

            @Override
            public int throughput() {
                return 100;
            }

            @Override
            public PercentileRequirement[] percentileRequirements() {
                return new PercentileRequirement[0];
            }

            @Override
            public int median() {
                return 100;
            }

            @Override
            public int max() {
                return max;
            }

            @Override
            public int average() {
                return 100;
            }
        };
        StatisticsProvider statisticsProvider = new StatisticsProvider() {

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
                "id", 500);
    }

    private static PerformanceRequirementValidator newValidator(
            PerformanceRequirements requirements,
            StatisticsProvider statisticsProvider) {
        return new PerformanceRequirementValidator(requirements,
                statisticsProvider, new PerfTestFailureFactory());
    }
}
