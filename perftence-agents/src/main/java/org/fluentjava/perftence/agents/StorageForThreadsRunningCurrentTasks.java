package org.fluentjava.perftence.agents;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fluentjava.perftence.common.Statistics;
import org.fluentjava.perftence.graph.GraphWriter;
import org.fluentjava.perftence.graph.GraphWriterProvider;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.reporting.ReportingOptions;
import org.fluentjava.perftence.reporting.summary.Summary;
import org.fluentjava.perftence.reporting.summary.SummaryAppender;

public final class StorageForThreadsRunningCurrentTasks implements GraphWriterProvider {

    private static final DecimalFormat DF = new DecimalFormat("####");

    private final ReportingOptions reportingOptions;
    private final List<Integer> threads;
    private final List<Long> times;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;

    public StorageForThreadsRunningCurrentTasks(final ReportingOptions reportingOptions,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        this.reportingOptions = reportingOptions;
        this.lineChartAdapterProvider = lineChartAdapterProvider;
        this.threads = Collections.synchronizedList(new ArrayList<Integer>());
        this.times = Collections.synchronizedList(new ArrayList<Long>());
    }

    public synchronized void store(final long time, final int threads) {
        this.threads().add(threads);
        this.times().add(time);
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

    private ImageData imageData() {
        final ImageData imageData = ImageData.noStatistics(reportingOptions().title(), reportingOptions().xAxisTitle(),
                lineChartAdapterProvider().forLineChart(reportingOptions().legendTitle()));
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

    private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.lineChartAdapterProvider;
    }

    private ReportingOptions reportingOptions() {
        return this.reportingOptions;
    }

    private Statistics statistics() {
        return Statistics.fromLatencies(threads());
    }

    public static StorageForThreadsRunningCurrentTasks newStorage(
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        return new StorageForThreadsRunningCurrentTasks(new ReportingOptions() {
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
        }, lineChartAdapterProvider);
    }

    public SummaryAppender summaryAppender() {
        return new SummaryAppender() {
            @Override
            public void append(final Summary<?> summary) {
                summary.bold("Statistics for threads running current tasks:").endOfLine();
                final Statistics statistics = statistics();
                summary.text("Max threads: " + statistics.max()).endOfLine();
                summary.text("Average threads: " + DF.format(statistics.mean())).endOfLine();
                summary.text("Median threads: " + statistics.median()).endOfLine();
                summary.endOfLine();
            }
        };
    }

    @Override
    public GraphWriter graphWriterFor(final String id) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), imageData());
            }

            @Override
            public String id() {
                return id + "-threads-running-tasks-currently";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !isEmpty();
            }
        };
    }
}