package net.sf.perftence.reporting.graph;

import java.util.Calendar;
import java.util.Date;

import net.sf.perftence.TestTimeAware;
import net.sf.perftence.reporting.LastSecondFailures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastSecondFailuresGraphWriter {
    protected static final Logger LOG = LoggerFactory
            .getLogger(LastSecondFailuresGraphWriter.class);
    private final LastSecondFailures failures;
    private final TestTimeAware testTimeAware;
    private final DatasetAdapterFactory datasetAdapterFactory;

    public LastSecondFailuresGraphWriter(final LastSecondFailures failures,
            final TestTimeAware testTimeAware,
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.failures = failures;
        this.testTimeAware = testTimeAware;
        this.datasetAdapterFactory = datasetAdapterFactory;
    }

    public GraphWriter graphFor(final String id) {
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
                final DatasetAdapter<LineChartGraphData> adapter = datasetAdapterFactory()
                        .forLineChart(title);
                final ImageData imageData = ImageData.noStatistics(title,
                        "Seconds", adapter);
                final Calendar calendar = Calendar.getInstance();
                final Date endTime = new Date(testTimeAware().startTime()
                        + testTimeAware().duration());
                calendar.setTime(new Date(testTimeAware().startTime()));
                long max = 0;
                log().debug("Failures = {}", failures().toString());
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

    private static Logger log() {
        return LOG;
    }

    private TestTimeAware testTimeAware() {
        return LastSecondFailuresGraphWriter.this.testTimeAware;
    }

    private LastSecondFailures failures() {
        return this.failures;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }
}
