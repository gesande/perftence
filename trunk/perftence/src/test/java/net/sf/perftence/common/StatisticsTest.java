package net.sf.perftence.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.common.Statistics;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsTest {

    private static Statistics stat;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(StatisticsTest.class);

    @BeforeClass
    public static void beforeClass() {
        stat = Statistics.fromLatencies(sampleList());
    }

    @SuppressWarnings("static-method")
    @Test
    public void mean() {
        checkMean();
        checkMean();
    }

    @SuppressWarnings("static-method")
    @Test
    public void median() {
        checkMedian();
        checkMedian();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile90() {
        check90percentile();
        check90percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile95() {
        check95percentile();
        check95percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile96() {
        check96percentile();
        check96percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile97() {
        check97percentile();
        check97percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile98() {
        check98percentile();
        check98percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void percentile99() {
        check99percentile();
        check99percentile();
    }

    @SuppressWarnings("static-method")
    @Test
    public void standardDeviation() {
        checkStandardDeviation();
        checkStandardDeviation();
    }

    @SuppressWarnings("static-method")
    @Test
    public void variance() {
        checkVariance();
        checkVariance();
    }

    @SuppressWarnings("static-method")
    @Test
    public void empty() {
        final Statistics empty = Statistics
                .fromLatencies(new ArrayList<Integer>());
        assertEquals(0, empty.max());
        assertEquals(0, empty.min());
        assertEquals(0, empty.mean(), 0);
        assertEquals(0, empty.median());
        assertEquals(0, empty.percentile90());
        assertEquals(0, empty.percentile95());
        assertEquals(0, empty.percentile96());
        assertEquals(0, empty.percentile97());
        assertEquals(0, empty.percentile98());
        assertEquals(0, empty.percentile99());
        assertEquals(Double.NaN, empty.variance(), 0);
        assertEquals(Double.NaN, empty.standardDeviation(), 0);
    }

    private static List<Integer> sampleList() {
        final List<Integer> list = new ArrayList<Integer>();
        list.add(600);
        list.add(470);
        list.add(170);
        list.add(430);
        list.add(300);
        return list;
    }

    private static void check90percentile() {
        assertEquals("90 percentile doesn't match!", 600, log(stat()
                .percentile90()), 0);
    }

    private static void check95percentile() {
        assertEquals("95 percentile doesn't match!", 600.00,
                log((double) stat().percentile95()), 0);
    }

    private static void check96percentile() {
        assertEquals("96 percentile doesn't match!", 600.00,
                log((double) stat().percentile96()), 0);
    }

    private static void check99percentile() {
        assertEquals("99 percentile doesn't match!", 600.0, log((double) stat()
                .percentile99()), 0);
    }

    private static void checkMean() {
        assertEquals("Mean doesn't match!", 394.00, log(stat().mean()), 0);
    }

    private static void checkMedian() {
        assertEquals("Median doesn't match!", 430.00, log((double) stat()
                .median()), 0);
    }

    private static void checkStandardDeviation() {
        assertEquals("Standard deviation doesn't match!", 147.32277488562315,
                log(stat().standardDeviation()), 0);
    }

    private static void checkVariance() {
        assertEquals("Variance doesn't match!", 21703.999999999996, log(stat()
                .variance()), 0);
    }

    private static void check98percentile() {
        assertEquals("97 percentile doesn't match!", 600.00,
                log((double) stat().percentile98()), 0);
    }

    private static void check97percentile() {
        assertEquals("97 percentile doesn't match!", 600.00,
                log((double) stat().percentile97()), 0);
    }

    private static Statistics stat() {
        return stat;
    }

    private static Logger log() {
        return LOGGER;
    }

    private static <T> T log(final T value) {
        log().info("Calculated value was {}", value);
        return value;
    }
}
