package net.sf.perftence.fluent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EstimatedInvocationsTest {

    @Test
    public void calculateWhenSampleCountIsLowerThanEstimated() {
        final int currentThroughput = 100;
        final int duration = 10010;
        final int sampleCount = 1000;
        final long result = new EstimatedInvocations().calculate(currentThroughput, duration, sampleCount);
        assertEquals(1001, result);
    }

    @Test
    public void calculateWhenSampleCountIsGreaterThanEstimated() {
        final int currentThroughput = 100;
        final int duration = 10009;
        final int sampleCount = 1000;
        final long result = new EstimatedInvocations().calculate(currentThroughput, duration, sampleCount);
        assertEquals(1000, result);
    }

}
