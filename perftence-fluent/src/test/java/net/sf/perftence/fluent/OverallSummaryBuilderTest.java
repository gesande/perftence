package net.sf.perftence.fluent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;

import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.formatting.FieldFormatter;
import net.sf.perftence.graph.GraphWriter;
import net.sf.perftence.reporting.Duration;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.SummaryAppender;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.setup.PerformanceTestSetup;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverallSummaryBuilderTest {
    private final static Logger LOG = LoggerFactory
            .getLogger(OverallSummaryBuilderTest.class);
    private final static DecimalFormat DF = new DecimalFormat("###.##");

    @SuppressWarnings("static-method")
    @Test
    public void overAllSummaryOfADurationBasedTest() {
        OverallSummaryBuilder builder = new OverallSummaryBuilder(
                durationBased(), statistics(), newSummaryFieldFactory(),
                new EstimatedInvocations());
        final String build = log(builder.build());
        assertNotNull("Summary was null!", build);
        assertTrue("samples field is missing!",
                build.contains("samples:                 286/286\n"));
        assertTrue("max field is missing!",
                build.contains("max:                     998\n"));
        assertTrue(
                "average field is missing!",
                build.contains("average:                 " + DF.format(508.38)
                        + "\n"));
        assertTrue("median field is missing!",
                build.contains("median:                  488\n"));
        assertTrue("95 percentage field is missing!",
                build.contains("95 percentile:           955\n"));
        assertTrue(
                "throughput field is missing!",
                build.contains("throughput:              " + DF.format(19.08)
                        + "\n"));
        assertTrue("execution time field is missing!",
                build.contains("execution time (ms):     15000\n"));
        assertTrue("threads field is missing!",
                build.contains("threads:                 10\n"));
        final String expected = "" + "samples:                 286/286\n"
                + "max:                     998\n"
                + "average:                 " + DF.format(508.38) + "\n"
                + "median:                  488\n"
                + "95 percentile:           955\n"
                + "throughput:              " + DF.format(19.08) + "\n"
                + "execution time (ms):     15000\n"
                + "threads:                 10\n";
        assertEquals(expected, build);
    }

    @SuppressWarnings("static-method")
    @Test
    public void overAllSummaryOfAThreadBasedTest() {
        OverallSummaryBuilder builder = new OverallSummaryBuilder(
                threadBased(), statistics(), newSummaryFieldFactory(),
                new EstimatedInvocations());
        final String build = log(builder.build());
        assertNotNull("Summary was null!", build);
        assertTrue("samples field is missing!",
                build.contains("samples:                 286/286\n"));
        assertTrue("max field is missing!",
                build.contains("max:                     998\n"));
        assertTrue(
                "average field is missing!",
                build.contains("average:                 " + DF.format(508.38)
                        + "\n"));
        assertTrue("median field is missing!",
                build.contains("median:                  488\n"));
        assertTrue("95 percentage field is missing!",
                build.contains("95 percentile:           955\n"));
        assertTrue(
                "throughput field is missing!",
                build.contains("throughput:              " + DF.format(19.08)
                        + "\n"));
        assertTrue("execution time field is missing!",
                build.contains("execution time (ms):     15000\n"));
        assertTrue("threads field is missing!",
                build.contains("threads:                 10\n"));
        final String expected = "" + "samples:                 286/286\n"
                + "max:                     998\n"
                + "average:                 " + DF.format(508.38) + "\n"
                + "median:                  488\n"
                + "95 percentile:           955\n"
                + "throughput:              " + DF.format(19.08) + "\n"
                + "execution time (ms):     15000\n"
                + "threads:                 10\n";
        assertEquals(expected, build);

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
                return 19.083234423432432;
            }

            @Override
            public long duration() {
                return 15000;
            }

            @Override
            public double averageLatency() {
                return 508.383234324324342;
            }
        };
    }

    private static PerformanceTestSetup durationBased() {
        return new PerformanceTestSetup() {

            @Override
            public int throughputRange() {
                return 0;
            }

            @Override
            public int threads() {
                return 10;
            }

            @Override
            public Collection<SummaryAppender> summaryAppenders() {
                return Collections.emptyList();
            }

            @Override
            public int invocations() {
                return 0;
            }

            @Override
            public int invocationRange() {
                return 0;
            }

            @Override
            public Collection<GraphWriter> graphWriters() {
                return Collections.emptyList();
            }

            @Override
            public int duration() {
                return Duration.seconds(15);
            }
        };
    }

    private static PerformanceTestSetup threadBased() {
        return new PerformanceTestSetup() {

            @Override
            public int throughputRange() {
                return 0;
            }

            @Override
            public int threads() {
                return 10;
            }

            @Override
            public Collection<SummaryAppender> summaryAppenders() {
                return Collections.emptyList();
            }

            @Override
            public int invocations() {
                return 286;
            }

            @Override
            public int invocationRange() {
                return 1000;
            }

            @Override
            public Collection<GraphWriter> graphWriters() {
                return Collections.emptyList();
            }

            @Override
            public int duration() {
                return 0;
            }
        };
    }

    private static SummaryFieldFactory newSummaryFieldFactory() {
        return SummaryFieldFactory.create(new FieldFormatter(),
                new FieldAdjuster());
    }
}
