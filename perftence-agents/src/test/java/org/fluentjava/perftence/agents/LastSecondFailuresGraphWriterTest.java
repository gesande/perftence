package org.fluentjava.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.fluentjava.perftence.TestTimeAware;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.GraphWriter;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.ImageFactoryUsingJFreeChart;
import org.fluentjava.perftence.graph.jfreechart.JFreeChartWriter;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.fluentjava.perftence.reporting.summary.LastSecondFailures;
import org.junit.Test;

public class LastSecondFailuresGraphWriterTest {

    @Test
    public void graph() throws InterruptedException {
        final LastSecondFailures failures = new LastSecondFailures(
                new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                        new AdjustedFieldBuilderFactory(new FieldFormatter(), new FieldAdjuster()).newInstance()));
        final long start = System.currentTimeMillis();
        Thread.sleep(1000);
        failures.more(new FailIHave());
        Thread.sleep(1000);
        failures.more(new FailIHave());
        failures.more(new FailIHave());
        Thread.sleep(2000);
        final long duration = System.currentTimeMillis() - start;
        final LastSecondFailuresGraphWriter lastSecondFailuresGraphWriter = new LastSecondFailuresGraphWriter(failures,
                new TestTimeAware() {

                    @Override
                    public long startTime() {
                        return start;
                    }

                    @Override
                    public long duration() {
                        return duration;
                    }
                }, new DefaultDatasetAdapterFactory());
        GraphWriter graphFor = lastSecondFailuresGraphWriter.graphWriterFor("testing-the-stuff");
        assertTrue(graphFor.hasSomethingToWrite());
        assertEquals("testing-the-stuff-last-second-failures", graphFor.id());
        final ImageFactoryUsingJFreeChart imageFactory = new ImageFactoryUsingJFreeChart(
                new JFreeChartWriter(HtmlTestReport.withDefaultReportPath().reportRootDirectory()));
        graphFor.writeImage(new ImageFactory() {

            @Override
            public void createXYLineChart(final String id, final ImageData imageData) {
                imageFactory.createXYLineChart(id, imageData);
                assertEquals("testing-the-stuff-last-second-failures", id);
                assertFalse(imageData.hasStatistics());
            }

            @Override
            public void createScatterPlot(final String id, final ImageData imageData) {
                throw new FailIHave("You shouldn't come here!");
            }

            @Override
            public void createBarChart(final String id, final ImageData imageData) {
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
