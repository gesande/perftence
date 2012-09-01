package net.sf.perftence.agents;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.perftence.reporting.ReportingOptions;
import net.sf.perftence.reporting.Statistics;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

public final class StorageForThreadsRunningCurrentTasks {

    private static final DecimalFormat DF = new DecimalFormat("####");

    private final ReportingOptions reportingOptions;
    private final List<Integer> threads;
    private final List<Long> times;
    private final String name;

    public StorageForThreadsRunningCurrentTasks(final String name,
            final ReportingOptions reportingOptions) {
        this.name = name;
        this.reportingOptions = reportingOptions;
        this.threads = Collections.synchronizedList(new ArrayList<Integer>());
        this.times = Collections.synchronizedList(new ArrayList<Long>());
    }

    public synchronized void store(final long time, final int threads) {
        this.threads().add(threads);
        this.times().add(time);
    }

    private String name() {
        return this.name;
    }

    private List<Long> times() {
        return this.times;
    }

    public boolean isEmpty() {
        return threads().isEmpty();
    }

    private List<Integer> threads() {
        return this.threads;
    }

    public ImageData imageData() {
        final ImageData imageData = ImageData.noStatistics(reportingOptions()
                .title(), reportingOptions().xAxisTitle(), reportingOptions()
                .legendTitle(), DatasetAdapterFactory
                .adapterForLineChart(reportingOptions().legendTitle()));
        int i = 0;
        double max = reportingOptions().range();
        for (double value : threads()) {
            imageData.add(times().get(i), value);
            i++;
            if (value > max) {
                max = value;
            }
        }
        imageData.range(max + 10.0);
        return imageData;
    }

    private ReportingOptions reportingOptions() {
        return this.reportingOptions;
    }

    private Statistics statistics() {
        return Statistics.fromLatencies(threads());
    }

    public static StorageForThreadsRunningCurrentTasks newStorage(
            final String name) {
        return new StorageForThreadsRunningCurrentTasks(name,
                new ReportingOptions() {
                    @Override
                    public String xAxisTitle() {
                        return "Time";
                    }

                    @Override
                    public String title() {
                        return "Threads running tasks currently";
                    }

                    @Override
                    public int range() {
                        return 250;
                    }

                    @Override
                    public boolean provideStatistics() {
                        return false;
                    }

                    @Override
                    public String legendTitle() {
                        return "Threads";
                    }
                });
    }

    public SummaryAppender summaryAppender() {
        return new SummaryAppender() {
            @Override
            public void append(final Summary<?> summary) {
                summary.bold("Statistics for threads running current tasks:")
                        .endOfLine();
                final Statistics statistics = statistics();
                summary.text("Max threads: " + statistics.max()).endOfLine();
                summary.text("Average threads: " + DF.format(statistics.mean()))
                        .endOfLine();
                summary.text("Median threads: " + statistics.median())
                        .endOfLine();
                summary.endOfLine();
            }
        };
    }

    public GraphWriter graphWriter() {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), imageData());
            }

            @Override
            public String id() {
                return name() + "-threads-running-tasks-currently";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !isEmpty();
            }
        };
    }
}