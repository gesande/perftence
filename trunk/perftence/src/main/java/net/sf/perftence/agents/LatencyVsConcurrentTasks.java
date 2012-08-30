package net.sf.perftence.agents;

import net.sf.perftence.reporting.ReportingOptions;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;

public final class LatencyVsConcurrentTasks {

    private final String name;
    private final ImageData imageData;
    private boolean hasSamples = false;

    private LatencyVsConcurrentTasks(final String name,
            final ImageData imageData) {
        this.name = name;
        this.imageData = imageData;
    }

    private String name() {
        return this.name;
    }

    private ImageData imageData() {
        return this.imageData;
    }

    public synchronized void report(final double tasks, final double latency) {
        if (!this.hasSamples) {
            this.hasSamples = true;
        }
        imageData().add(tasks, latency);
    }

    private boolean hasSamples() {
        return this.hasSamples;
    }

    public static LatencyVsConcurrentTasks instance(final String name) {
        ReportingOptions reportingOptions = new ReportingOptions() {

            @Override
            public String xAxisTitle() {
                return "Tasks";
            }

            @Override
            public String title() {
                return "Latencies vs number of concurrent tasks";
            }

            @Override
            public int range() {
                return 0;
            }

            @Override
            public boolean provideStatistics() {
                return false;
            }

            @Override
            public String legendTitle() {
                return "Latency / number of concurrent tasks";
            }
        };
        final String legendTitle = reportingOptions.legendTitle();
        return new LatencyVsConcurrentTasks(name, ImageData.noStatistics(
                reportingOptions.title(), reportingOptions.xAxisTitle(),
                legendTitle, DatasetAdapterFactory.adapterForScatterPlot(
                        legendTitle, yAxisTitle())));
    }

    private static String yAxisTitle() {
        return "Latency";
    }

    public GraphWriter graphWriter() {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createScatterPlot(id(), imageData());
            }

            @Override
            public String id() {
                return name() + "-latencies-vs-concurrenttasks-scatter";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return hasSamples();
            }
        };
    }

}
