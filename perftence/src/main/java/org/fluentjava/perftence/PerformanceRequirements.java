package org.fluentjava.perftence;

public interface PerformanceRequirements {
    int average();

    int median();

    int max();

    long totalTime();

    int throughput();

    PercentileRequirement[] percentileRequirements();

}