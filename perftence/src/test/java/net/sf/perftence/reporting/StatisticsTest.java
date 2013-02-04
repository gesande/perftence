package net.sf.perftence.reporting;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

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

    private static void check98percentile() {
        Assert.assertEquals("97 percentile doesn't match!", 600.00,
                log((double) stat().percentile98()));
    }

    private static void check97percentile() {
        Assert.assertEquals("97 percentile doesn't match!", 600.00,
                log((double) stat().percentile97()));
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

    private static <T> T log(final T value) {
        log().info("Calculated value was {}", value);
        return value;
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
        Assert.assertEquals("90 percentile doesn't match!", 600.00,
                log((double) stat().percentile90()));
    }

    private static void check95percentile() {
        Assert.assertEquals("95 percentile doesn't match!", 600.00,
                log((double) stat().percentile95()));
    }

    private static void check96percentile() {
        Assert.assertEquals("96 percentile doesn't match!", 600.00,
                log((double) stat().percentile96()));
    }

    private static void check99percentile() {
        Assert.assertEquals("99 percentile doesn't match!", 600.00,
                log((double) stat().percentile99()));
    }

    private static void checkMean() {
        Assert.assertEquals("Mean doesn't match!", 394.00, log(stat().mean()));
    }

    private static void checkMedian() {
        Assert.assertEquals("Median doesn't match!", 430.00,
                log((double) stat().median()));
    }

    private static void checkStandardDeviation() {
        Assert.assertEquals("Standard deviation doesn't match!",
                147.32277488562315, log(stat().standardDeviation()));
    }

    private static void checkVariance() {
        Assert.assertEquals("Variance doesn't match!", 21703.999999999996,
                log(stat().variance()));
    }

    private static Statistics stat() {
        return stat;
    }

    private static Logger log() {
        return LOGGER;
    }

}
