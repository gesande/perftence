package net.sf.perftence;

import net.sf.perftence.LastSecondStatistics;

import org.junit.Test;

public class LastSecondStatisticsTest {
    private LastSecondStatistics statistics;

    @Test
    public void test() throws InterruptedException {
        this.statistics = new LastSecondStatistics();

        long start = System.currentTimeMillis() / 1000;
        System.out.println("start : " + start);
        Thread.sleep(1000);
        this.statistics.latency(500);
        this.statistics.latency(500);
        double averageLatency = this.statistics.averageLatency();
        double currentThroughput = this.statistics.currentThroughput();
        long currentDuration = this.statistics.currentDuration();
        long sampleCount = this.statistics.sampleCount();
        System.out.println("\nstats:\n");
        System.out.println("averageLatency : " + averageLatency);
        System.out.println("currentThroughput : " + currentThroughput);
        System.out.println("currentDuration : " + currentDuration);
        System.out.println("sampleCount : " + sampleCount);

        Thread.sleep(1000);
        long currentSecond = System.currentTimeMillis() / 1000;
        System.out.println("currentSecond : " + currentSecond);
        this.statistics.latency(250);
        this.statistics.latency(250);
        Thread.sleep(400);
        averageLatency = this.statistics.averageLatency();
        currentThroughput = this.statistics.currentThroughput();
        currentDuration = this.statistics.currentDuration();
        sampleCount = this.statistics.sampleCount();
        System.out.println("\nstats:\n");
        System.out.println("averageLatency : " + averageLatency);
        System.out.println("currentThroughput : " + currentThroughput);
        System.out.println("currentDuration : " + currentDuration);
        System.out.println("sampleCount : " + sampleCount);
        Thread.sleep(1000);

        long lastSecond = (System.currentTimeMillis() / 1000) - 1;
        System.out.println("lastSecond : " + lastSecond);
        this.statistics.latency(300);
        this.statistics.latency(300);
        averageLatency = this.statistics.averageLatency();
        currentThroughput = this.statistics.currentThroughput();
        currentDuration = this.statistics.currentDuration();
        sampleCount = this.statistics.sampleCount();
        System.out.println("\nstats:\n");
        System.out.println("averageLatency : " + averageLatency);
        System.out.println("currentThroughput : " + currentThroughput);
        System.out.println("currentDuration : " + currentDuration);
        System.out.println("sampleCount : " + sampleCount);

    }
}
