package org.fluentjava.perftence.agents;

import org.fluentjava.perftence.graph.GraphWriter;
import org.fluentjava.perftence.graph.GraphWriterProvider;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;
import org.fluentjava.perftence.reporting.ReportingOptions;

public final class LatencyVsConcurrentTasks implements GraphWriterProvider {

    private final ImageData imageData;
    private boolean hasSamples = false;

    private LatencyVsConcurrentTasks(final ImageData imageData) {
        this.imageData = imageData;
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

    public static LatencyVsConcurrentTasks instance(final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider) {
        final ReportingOptions reportingOptions = new ReportingOptions() {

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
        return new LatencyVsConcurrentTasks(ImageData.noStatistics(reportingOptions.title(),
                reportingOptions.xAxisTitle(), scatterPlotAdapterProvider.forScatterPlot(legendTitle, yAxisTitle())));
    }

    private static String yAxisTitle() {
        return "Latency";
    }

    @Override
    public GraphWriter graphWriterFor(final String id) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createScatterPlot(id(), imageData());
            }

            @Override
            public String id() {
                return id + "-latencies-vs-concurrenttasks-scatter";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return hasSamples();
            }
        };
    }

}
