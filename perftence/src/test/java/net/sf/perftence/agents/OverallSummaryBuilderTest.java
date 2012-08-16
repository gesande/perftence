package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.FieldFormatter;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverallSummaryBuilderTest implements TestFailureNotifier {
    private final static Logger LOG = LoggerFactory
            .getLogger(OverallSummaryBuilderTest.class);

    @Test
    public void buildSummary() {
        final OverallSummaryBuilder builder = new OverallSummaryBuilder(
                failureNotifier(false), statistics(), newSummaryFieldFactory());
        final String build = log(builder.build());
        assertNotNull("Summary was null!", build);
        assertTrue("finished tasks field is missing!",
                build.contains("finished tasks:          286\n"));
        assertTrue("failed tasks field is missing!",
                build.contains("failed tasks:            0\n"));
        assertTrue("threads field is missing!",
                build.contains("max:                     998\n"));
        assertTrue("average field is missing!",
                build.contains("average:                 508.38\n"));
        assertTrue("median field is missing!",
                build.contains("median:                  488\n"));
        assertTrue("95 percentile field is missing!",
                build.contains("95 percentile:           955\n"));
        assertTrue("throughput field is missing!",
                build.contains("throughput:              19.08\n"));
        assertTrue("execution time field is missing!",
                build.contains("execution time (ms):     15000\n"));
        final String expected = "" + "finished tasks:          286\n"
                + "failed tasks:            0\n"
                + "max:                     998\n"
                + "average:                 508.38\n"
                + "median:                  488\n"
                + "95 percentile:           955\n"
                + "throughput:              19.08\n"
                + "execution time (ms):     15000\n";
        assertEquals(expected, build);
    }

    @Test
    public void buildSummaryWithFailures() {
        final OverallSummaryBuilder builder = new OverallSummaryBuilder(
                failureNotifier(true), statistics(), newSummaryFieldFactory());
        final String build = log(builder.build());
        assertNotNull("Summary was null!", build);
        assertTrue("finished tasks field is missing!",
                build.contains("finished tasks:          286\n"));
        assertTrue("failed tasks field is missing!",
                build.contains("failed tasks:            1\n"));
        assertTrue("threads field is missing!",
                build.contains("max:                     998\n"));
        assertTrue("average field is missing!",
                build.contains("average:                 508.38\n"));
        assertTrue("median field is missing!",
                build.contains("median:                  488\n"));
        assertTrue("95 percentile field is missing!",
                build.contains("95 percentile:           955\n"));
        assertTrue("throughput field is missing!",
                build.contains("throughput:              19.08\n"));
        assertTrue("execution time field is missing!",
                build.contains("execution time (ms):     15000\n"));
        final String expected = "" + "finished tasks:          286\n"
                + "failed tasks:            1\n"
                + "max:                     998\n"
                + "average:                 508.38\n"
                + "median:                  488\n"
                + "95 percentile:           955\n"
                + "throughput:              19.08\n"
                + "execution time (ms):     15000\n";
        assertEquals(expected, build);
    }

    private TestFailureNotifierDecorator failureNotifier(final boolean failure) {
        final TestFailureNotifierDecorator failures = new TestFailureNotifierDecorator(
                this);
        if (failure) {
            failures.testFailed(new NullPointerException());
        }
        return failures;
    }

    private static String log(final String build) {
        log().info("{}{}", "\n", build);
        return build;
    }

    private static Logger log() {
        return LOG;
    }

    private static StatisticsProvider statistics() {
        return new StatisticsProvider() {

            @Override
            public long sampleCount() {
                return 286;
            }

            @Override
            public long percentileLatency(int percentile) {
                return 955;
            }

            @Override
            public long minLatency() {
                return 0;
            }

            @Override
            public long median() {
                return 488;
            }

            @Override
            public long maxLatency() {
                return 998;
            }

            @Override
            public boolean hasSamples() {
                return true;
            }

            @Override
            public double throughput() {
                return 19.08;
            }

            @Override
            public long duration() {
                return 15000;
            }

            @Override
            public double averageLatency() {
                return 508.38;
            }
        };
    }

    @Override
    public void testFailed(final Throwable t) {
        log().info("Test failure reported: {}", t.getClass().getName());
    }

    private static SummaryFieldFactoryForAgentBasedTests newSummaryFieldFactory() {
        return new SummaryFieldFactoryForAgentBasedTests(
                SummaryFieldFactory.create(new FieldFormatter(),
                        new FieldAdjuster()));
    }
}
