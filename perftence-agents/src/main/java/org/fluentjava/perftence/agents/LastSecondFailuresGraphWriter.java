package org.fluentjava.perftence.agents;

import java.util.Calendar;
import java.util.Date;

import org.fluentjava.perftence.TestTimeAware;
import org.fluentjava.perftence.common.ImageDataFactory;
import org.fluentjava.perftence.graph.GraphWriter;
import org.fluentjava.perftence.graph.GraphWriterProvider;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.reporting.summary.LastSecondFailures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastSecondFailuresGraphWriter implements GraphWriterProvider {
    protected static final Logger LOG = LoggerFactory.getLogger(LastSecondFailuresGraphWriter.class);
    private final LastSecondFailures failures;
    private final TestTimeAware testTimeAware;
    private final ImageDataFactory imageDataFactory;

    public LastSecondFailuresGraphWriter(final LastSecondFailures failures, final TestTimeAware testTimeAware,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        this.failures = failures;
        this.testTimeAware = testTimeAware;
        this.imageDataFactory = new ImageDataFactory(lineChartAdapterProvider);
    }

    @Override
    public GraphWriter graphWriterFor(final String id) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), data());
            }

            @Override
            public String id() {
                return id + "-last-second-failures";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return failures().hasFailures();
            }

            private ImageData data() {
                final String title = "Last second failures";
                final String xAxisTitle = "Seconds";
                final ImageData imageData = newImageDataForLinechart(title, xAxisTitle);
                final Calendar calendar = Calendar.getInstance();
                final Date endTime = new Date(testTimeAware().startTime() + testTimeAware().duration());
                calendar.setTime(new Date(testTimeAware().startTime()));
                long max = 0;
                LOG.debug("Failures = {}", failures().toString());
                while (calendar.getTime().before(endTime)) {
                    final long time = calendar.getTimeInMillis();
                    final long failures = failures().failuresFor(time);
                    imageData.add(time, failures);
                    calendar.add(Calendar.MILLISECOND, 1000);
                    if (failures > max) {
                        max = failures;
                    }
                }
                return imageData.range(max + 10.0);
            }

        };
    }

    private ImageData newImageDataForLinechart(final String title, final String xAxisTitle) {
        return this.imageDataFactory.newImageDataForLineChart(title, xAxisTitle);
    }

    private TestTimeAware testTimeAware() {
        return this.testTimeAware;
    }

    private LastSecondFailures failures() {
        return this.failures;
    }

}
