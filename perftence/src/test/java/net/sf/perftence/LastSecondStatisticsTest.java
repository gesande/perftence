package net.sf.perftence;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastSecondStatisticsTest {
    private final static Logger LOG = LoggerFactory
            .getLogger(LastSecondStatisticsTest.class);

    @SuppressWarnings("static-method")
    @Test
    public void test() throws InterruptedException {
        final LastSecondStatistics statistics = new LastSecondStatistics();

        final long start = System.currentTimeMillis() / 1000;
        System.out.println("start : " + start);
        Thread.sleep(1000);
        statistics.latency(500);
        statistics.latency(500);
        double averageLatency = statistics.averageLatency();
        double currentThroughput = statistics.currentThroughput();
        long currentDuration = statistics.currentDuration();
        long sampleCount = statistics.sampleCount();
        log().info("\nstats:\n");
        log().info("averageLatency : " + averageLatency);
        log().info("currentThroughput : " + currentThroughput);
        log().info("currentDuration : " + currentDuration);
        log().info("sampleCount : " + sampleCount);

        Thread.sleep(1000);
        final long currentSecond = System.currentTimeMillis() / 1000;
        log().info("currentSecond : " + currentSecond);
        statistics.latency(250);
        statistics.latency(250);
        Thread.sleep(400);
        averageLatency = statistics.averageLatency();
        currentThroughput = statistics.currentThroughput();
        currentDuration = statistics.currentDuration();
        sampleCount = statistics.sampleCount();
        log().info("\nstats:\n");
        log().info("averageLatency : " + averageLatency);
        log().info("currentThroughput : " + currentThroughput);
        log().info("currentDuration : " + currentDuration);
        log().info("sampleCount : " + sampleCount);
        Thread.sleep(1000);

        final long lastSecond = (System.currentTimeMillis() / 1000) - 1;
        log().info("lastSecond : " + lastSecond);
        statistics.latency(300);
        statistics.latency(300);
        averageLatency = statistics.averageLatency();
        currentThroughput = statistics.currentThroughput();
        currentDuration = statistics.currentDuration();
        sampleCount = statistics.sampleCount();
        log().info("\nstats:\n");
        log().info("averageLatency : " + averageLatency);
        log().info("currentThroughput : " + currentThroughput);
        log().info("currentDuration : " + currentDuration);
        log().info("sampleCount : " + sampleCount);
        log().info("95 percentile : " + statistics.percentileLatency(95));
    }

    private static Logger log() {
        return LOG;
    }
}
