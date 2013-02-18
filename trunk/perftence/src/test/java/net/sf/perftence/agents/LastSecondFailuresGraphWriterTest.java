package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.perftence.TestTimeAware;
import net.sf.perftence.common.FailedInvocationsFactory;
import net.sf.perftence.common.LastSecondFailures;
import net.sf.perftence.formatting.DefaultDoubleFormatter;
import net.sf.perftence.formatting.FieldFormatter;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.graph.jfreechart.ImageFactoryUsingJFreeChart;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;

import org.junit.Test;

public class LastSecondFailuresGraphWriterTest {

    @SuppressWarnings("static-method")
    @Test
    public void graph() throws InterruptedException {
        final LastSecondFailures failures = new LastSecondFailures(
                new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                        new AdjustedFieldBuilderFactory(new FieldFormatter(),
                                new FieldAdjuster()).newInstance()));
        final long start = System.currentTimeMillis();
        Thread.sleep(1000);
        failures.more(new FailIHave());
        Thread.sleep(1000);
        failures.more(new FailIHave());
        failures.more(new FailIHave());
        Thread.sleep(2000);
        final long duration = System.currentTimeMillis() - start;
        final LastSecondFailuresGraphWriter lastSecondFailuresGraphWriter = new LastSecondFailuresGraphWriter(
                failures, new TestTimeAware() {

                    @Override
                    public long startTime() {
                        return start;
                    }

                    @Override
                    public long duration() {
                        return duration;
                    }
                }, new DefaultDatasetAdapterFactory());
        GraphWriter graphFor = lastSecondFailuresGraphWriter
                .graphWriterFor("testing-the-stuff");
        assertTrue(graphFor.hasSomethingToWrite());
        assertEquals("testing-the-stuff-last-second-failures", graphFor.id());
        final ImageFactoryUsingJFreeChart imageFactory = new ImageFactoryUsingJFreeChart();
        graphFor.writeImage(new ImageFactory() {

            @Override
            public void updateIndexFile(final String id) {
                throw new FailIHave("You shouldn't come here!");
            }

            @Override
            public void createXYLineChart(final String id,
                    final ImageData imageData) {
                imageFactory.createXYLineChart(id, imageData);
                assertEquals("testing-the-stuff-last-second-failures", id);
                assertFalse(imageData.hasStatistics());
            }

            @Override
            public void createScatterPlot(final String id,
                    final ImageData imageData) {
                throw new FailIHave("You shouldn't come here!");
            }

            @Override
            public void createBarChart(final String id,
                    final ImageData imageData) {
                throw new FailIHave("You shouldn't come here!");
            }
        });
    }

    private final static class FailIHave extends RuntimeException {

        public FailIHave() {
        }

        public FailIHave(final String msg) {
            super(msg);
        }

    }
}
