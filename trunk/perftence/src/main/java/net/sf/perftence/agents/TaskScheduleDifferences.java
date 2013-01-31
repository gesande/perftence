package net.sf.perftence.agents;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.sf.perftence.reporting.ReportingOptions;
import net.sf.perftence.reporting.Statistics;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.bag.TreeBag;

public final class TaskScheduleDifferences {
    private static final DecimalFormat DF = new DecimalFormat("####");

    private final SortedBag differencies;
    private final String name;
    private final ReportingOptions reportingOptions;
    private final DatasetAdapterFactory datasetAdapterFactory;

    public TaskScheduleDifferences(final String name,
            final ReportingOptions reportingOptions,
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.name = name;
        this.reportingOptions = reportingOptions;
        this.differencies = SynchronizedSortedBag.decorate(new TreeBag());
        this.datasetAdapterFactory = datasetAdapterFactory;
    }

    private ReportingOptions reportingOptions() {
        return this.reportingOptions;
    }

    private SortedBag differencies() {
        return this.differencies;
    }

    private String name() {
        return this.name;
    }

    /**
     * @param difference
     *            difference between scheduled and actual in nano time
     */
    public void report(final long difference) {
        differencies().add(difference);
    }

    public static TaskScheduleDifferences instance(final String name,
            DatasetAdapterFactory datasetAdapterFactory) {
        return new TaskScheduleDifferences(name, new ReportingOptions() {

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
        final Set<Long> uniqueSet = uniqueSamples();
        long max = 0;
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
        return this.differencies.getCount(value);
    }

    @SuppressWarnings("unchecked")
    private Set<Long> uniqueSamples() {
        return this.differencies.uniqueSet();
    }

    @Override
    public String toString() {
        return "size = " + this.differencies.size() + "\n"
                + this.differencies.toString();
    }

    public GraphWriter graphWriter() {
        return new GraphWriter() {

            @Override
            public void writeImage(ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), imageData());
            }

            @Override
            public String id() {
                return name() + "-task-schedule-differences";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !TaskScheduleDifferences.this.differencies.isEmpty();
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
        final Set<Long> uniqueSamples = uniqueSamples();
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