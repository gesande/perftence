package net.sf.perftence.agents;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.sf.perftence.bag.StronglyTypedSortedBag;
import net.sf.perftence.common.Statistics;
import net.sf.perftence.graph.DatasetAdapterFactory;
import net.sf.perftence.graph.GraphWriter;
import net.sf.perftence.graph.GraphWriterProvider;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.reporting.ReportingOptions;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

public final class TaskScheduleDifferences implements GraphWriterProvider {
    private static final DecimalFormat DF = new DecimalFormat("####");

    private final StronglyTypedSortedBag<Long> differencies;
    private final ReportingOptions reportingOptions;
    private final DatasetAdapterFactory datasetAdapterFactory;

    public TaskScheduleDifferences(final ReportingOptions reportingOptions,
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.reportingOptions = reportingOptions;
        this.differencies = StronglyTypedSortedBag.synchronizedTreeBag();
        this.datasetAdapterFactory = datasetAdapterFactory;
    }

    private ReportingOptions reportingOptions() {
        return this.reportingOptions;
    }

    private StronglyTypedSortedBag<Long> differencies() {
        return this.differencies;
    }

    /**
     * @param difference
     *            difference between scheduled and actual in nano time
     */
    public void report(final long difference) {
        differencies().add(difference);
    }

    public static TaskScheduleDifferences instance(
            DatasetAdapterFactory datasetAdapterFactory) {
        return new TaskScheduleDifferences(new ReportingOptions() {

            @Override
            public String xAxisTitle() {
                return "Time difference (in millis)";
            }

            @Override
            public String title() {
                return "Time difference (in millis) between scheduled (when) and actual time the task was run";
            }

            @Override
            public int range() {
                return 100;
            }

            @Override
            public boolean provideStatistics() {
                return false;
            }

            @Override
            public String legendTitle() {
                return "Frequency";
            }
        }, datasetAdapterFactory);
    }

    private ImageData imageData() {
        final ImageData imageData = ImageData.noStatistics(
                reportingOptions().title(),
                reportingOptions().xAxisTitle(),
                datasetAdapterFactory().forLineChart(
                        reportingOptions().legendTitle()));
        final Collection<Long> uniqueSet = uniqueSamples();
        long max = reportingOptions().range();
        for (final Long difference : uniqueSet) {
            final int value = count(difference);
            imageData.add(convertToMillis(difference), value);
            if (value > max) {
                max = value;
            }
        }
        return imageData.range(max);
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    private int count(final long value) {
        return differencies().count(value);
    }

    private Collection<Long> uniqueSamples() {
        return differencies().uniqueSamples();
    }

    @Override
    public String toString() {
        return "size = " + differencies().size() + "\n"
                + differencies().toString();
    }

    @Override
    public GraphWriter graphWriterFor(final String id) {
        return new GraphWriter() {

            @Override
            public void writeImage(ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), imageData());
            }

            @Override
            public String id() {
                return id + "-task-schedule-differences";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !differencies().isEmpty();
            }
        };
    }

    public SummaryAppender summaryAppender() {
        return new SummaryAppender() {

            @Override
            public void append(final Summary<?> summary) {
                summary.bold(
                        "Time difference (in millis) between isTimeToRun and actual time the task was run")
                        .endOfLine();
                final Statistics statistics = statistics();
                summary.text("Max: " + statistics.max()).endOfLine();
                summary.text("Min: " + statistics.min()).endOfLine();
                summary.text("Average: " + DF.format(statistics.mean()))
                        .endOfLine();
                summary.text("Median: " + statistics.median()).endOfLine();
                summary.endOfLine();
            }
        };
    }

    private Statistics statistics() {
        final Collection<Long> uniqueSamples = uniqueSamples();
        final List<Integer> latencies = new ArrayList<Integer>();
        for (final Long sample : uniqueSamples) {
            latencies.add(Integer.parseInt(Long
                    .toString(convertToMillis(sample))));
        }
        return Statistics.fromLatencies(latencies);
    }

    private static long convertToMillis(final long sample) {
        return TimeUnit.MILLISECONDS.convert(sample, TimeUnit.NANOSECONDS);
    }

}
