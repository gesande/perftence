package net.sf.perftence.common;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.DatasetAdapterFactory;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.jfreechart.LineChartGraphData;
import net.sf.perftence.reporting.ReportingOptions;

public final class DefaultInvocationStorage implements InvocationStorage {

    private final List<Integer> totalInvocations;
    private boolean reportedLatencyBeingBelowOne = false;
    private final ReportingOptions reportingOptions;
    private final DatasetAdapterFactory datasetAdapterFactory;

    private DefaultInvocationStorage(final int totalInvocations,
            final ReportingOptions reportingOptions,
            DatasetAdapterFactory datasetAdapterFactory) {
        this.reportingOptions = reportingOptions;
        this.datasetAdapterFactory = datasetAdapterFactory;
        this.totalInvocations = initialize(totalInvocations);
    }

    public static InvocationStorage newDefaultStorage(
            final int totalInvocations,
            final ReportingOptions reportingOptions,
            final DatasetAdapterFactory datasetAdapterFactory) {
        return new DefaultInvocationStorage(totalInvocations, reportingOptions,
                datasetAdapterFactory);
    }

    private static List<Integer> initialize(final int invocations) {
        return invocations > 0 ? new ArrayList<Integer>(invocations)
                : new ArrayList<Integer>();
    }

    @Override
    public void store(final int latency) {
        reportLatencyBeingBelowOne(latency);
        addLatency(latency);
    }

    private boolean addLatency(final int latency) {
        return invocations().add(latency == 0 ? 1 : latency);
    }

    private void reportLatencyBeingBelowOne(final int latency) {
        if (latency == 0 && !reportedLatencyBeingBelowOne()) {
            markReportLatenciesBeingBelowOne();
        }
    }

    private synchronized void markReportLatenciesBeingBelowOne() {
        this.reportedLatencyBeingBelowOne = true;
    }

    @Override
    public synchronized boolean reportedLatencyBeingBelowOne() {
        return this.reportedLatencyBeingBelowOne;
    }

    @Override
    public Statistics statistics() {
        return Statistics.fromLatencies(invocations());
    }

    private List<Integer> invocations() {
        return this.totalInvocations;
    }

    @Override
    public boolean isEmpty() {
        return invocations().isEmpty();
    }

    @Override
    public ImageData imageData() {
        return imageData(invocations());
    }

    private ImageData imageData(final List<Integer> invocations) {
        final DatasetAdapter<LineChartGraphData> adapter = datasetAdapterFactory()
                .forLineChart(legendTitle());
        final ImageData imageData = provideStatistics() ? ImageData.statistics(
                title(), xAxisTitle(), range(), statistics(), adapter)
                : ImageData.noStatistics(title(), xAxisTitle(), range(),
                        adapter);
        int i = 0;
        for (final Integer latency : invocations) {
            imageData.add(i, latency);
            i++;
        }
        return imageData;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    private int range() {
        return reportingOptions().range();
    }

    private boolean provideStatistics() {
        return reportingOptions().provideStatistics();
    }

    private ReportingOptions reportingOptions() {
        return this.reportingOptions;
    }

    private String legendTitle() {
        return reportingOptions().legendTitle();
    }

    private String xAxisTitle() {
        return reportingOptions().xAxisTitle();
    }

    private String title() {
        return reportingOptions().title();
    }

    public static InvocationStorage invocationStorageWithNoSamples(
            final DatasetAdapterFactory datasetAdapterFactory) {
        return new InvocationStorage() {
            @Override
            public void store(final int latency) {
                // left empty intentionally
            }

            @Override
            public Statistics statistics() {
                return Statistics.fromLatencies(new ArrayList<Integer>());
            }

            @Override
            public boolean reportedLatencyBeingBelowOne() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public ImageData imageData() {
                return ImageData.statistics("no samples", "X-axis title", 100,
                        statistics(),
                        datasetAdapterFactory.forLineChart("legend title"));
            }
        };
    }
}
