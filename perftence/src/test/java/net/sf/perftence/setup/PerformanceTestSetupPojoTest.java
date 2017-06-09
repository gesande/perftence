package net.sf.perftence.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.sf.perftence.graph.GraphWriter;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

public class PerformanceTestSetupPojoTest {

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupDuration() {
        PerformanceTestSetupPojo.builder().noSetup().duration();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupGraphWriters() {
        PerformanceTestSetupPojo.builder().noSetup().graphWriters();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupInvocationRange() {
        PerformanceTestSetupPojo.builder().noSetup().invocationRange();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupInvocations() {
        PerformanceTestSetupPojo.builder().noSetup().invocations();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupSummaryAppenders() {
        PerformanceTestSetupPojo.builder().noSetup().summaryAppenders();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupThreads() {
        PerformanceTestSetupPojo.builder().noSetup().threads();
    }

    @Test(expected = NoTestSetupDefined.class)
    public void noSetupThroughputRange() {
        PerformanceTestSetupPojo.builder().noSetup().throughputRange();
    }

    @Test
    public void buildSetupWithNoRangesDefined() {
        final PerformanceTestSetup setup = PerformanceTestSetupPojo.builder().threads(10).duration(100)
                .invocations(1000).build();
        assertEquals(10, setup.threads());
        assertEquals(100, setup.duration());
        assertEquals(1000, setup.invocations());
        assertEquals(500, setup.invocationRange());
        assertEquals(2500, setup.throughputRange());
        assertEquals(0, setup.summaryAppenders().size());
        assertEquals(0, setup.graphWriters().size());
        assertEquals(
                "PerformanceTestSetupPojo [threads=10, invocations=1000, duration=100, invocationRange=500, throughputRange=2500, summaryAppenders=[], graphWriters=[]]",
                setup.toString());
    }

    @Test
    public void buildSetupWithRangesDefined() {
        final PerformanceTestSetup setup = PerformanceTestSetupPojo.builder().threads(10).duration(100)
                .invocations(1000).invocationRange(102).throughputRange(103).build();
        assertEquals(10, setup.threads());
        assertEquals(100, setup.duration());
        assertEquals(1000, setup.invocations());
        assertEquals(102, setup.invocationRange());
        assertEquals(103, setup.throughputRange());
        assertEquals(0, setup.summaryAppenders().size());
        assertEquals(0, setup.graphWriters().size());
        assertEquals(
                "PerformanceTestSetupPojo [threads=10, invocations=1000, duration=100, invocationRange=102, throughputRange=103, summaryAppenders=[], graphWriters=[]]",
                setup.toString());
    }

    @Test
    public void buildSetupWithSummaryAppender() {
        final SummaryAppender summaryAppender = new SummaryAppender() {

            @Override
            public void append(final Summary<?> summary) {
                // just be there
            }

            @Override
            public String toString() {
                return "being here for reference";
            }
        };
        final PerformanceTestSetup setup = PerformanceTestSetupPojo.builder().threads(10).duration(100)
                .invocations(1000).invocationRange(102).throughputRange(103).summaryAppender(summaryAppender).build();
        assertEquals(10, setup.threads());
        assertEquals(100, setup.duration());
        assertEquals(1000, setup.invocations());
        assertEquals(102, setup.invocationRange());
        assertEquals(103, setup.throughputRange());
        assertEquals(1, setup.summaryAppenders().size());
        assertTrue(setup.summaryAppenders().contains(summaryAppender));
        assertEquals(0, setup.graphWriters().size());
        assertEquals(
                "PerformanceTestSetupPojo [threads=10, invocations=1000, duration=100, invocationRange=102, throughputRange=103, summaryAppenders=[being here for reference], graphWriters=[]]",
                setup.toString());
    }

    @Test
    public void buildSetupWithGraphwriter() {
        final GraphWriter graphWriter = new GraphWriter() {

            @Override
            public void writeImage(ImageFactory imageFactory) {
                // just be there
            }

            @Override
            public String id() {
                return null;
            }

            @Override
            public String toString() {
                return "being here for reference";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return false;
            }
        };
        final PerformanceTestSetup setup = PerformanceTestSetupPojo.builder().threads(10).duration(100)
                .invocations(1000).invocationRange(102).throughputRange(103).graphWriter(graphWriter).build();
        assertEquals(10, setup.threads());
        assertEquals(100, setup.duration());
        assertEquals(1000, setup.invocations());
        assertEquals(102, setup.invocationRange());
        assertEquals(103, setup.throughputRange());
        assertEquals(0, setup.summaryAppenders().size());
        assertEquals(1, setup.graphWriters().size());
        assertTrue(setup.graphWriters().contains(graphWriter));
        assertEquals(
                "PerformanceTestSetupPojo [threads=10, invocations=1000, duration=100, invocationRange=102, throughputRange=103, summaryAppenders=[], graphWriters=[being here for reference]]",
                setup.toString());
    }
}
