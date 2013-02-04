package net.sf.perftence.reporting.summary;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.perftence.reporting.CustomFailureReporter;
import net.sf.perftence.reporting.FailedInvocations;
import net.sf.perftence.reporting.FailedInvocationsFactory;
import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.LineChartGraphData;

public final class LastSecondFailures implements CustomFailureReporter,
        CustomIntermediateSummaryProvider {
    private final Map<Long, FailedInvocations> failures = Collections
            .synchronizedMap(new HashMap<Long, FailedInvocations>());
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final DatasetAdapterFactory datasetAdapterFactory;

    public LastSecondFailures(
            final FailedInvocationsFactory failedInvocationsFactory,
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.failedInvocationsFactory = failedInvocationsFactory;
        this.datasetAdapterFactory = datasetAdapterFactory;
    }

    @Override
    public synchronized void more(final Throwable t) {
        final long currentSecond = currentSecond();
        if (failures().containsKey(currentSecond)) {
            failures().get(currentSecond).more(t);
        } else {
            final FailedInvocations failed = failedInvocationsFactory()
                    .newInstance();
            failed.more(t);
            failures().put(currentSecond, failed);
        }
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    private Map<Long, FailedInvocations> failures() {
        return this.failures;
    }

    private static long currentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public void provideIntermediateSummary(final IntermediateSummary summary) {
        final long lastSecond = currentSecond() - 1;
        if (failures().containsKey(lastSecond)) {
            failures().get(lastSecond).provideIntermediateSummary(summary);
        }
    }

    public GraphWriter graphFor(final String name) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), data());
            }

            private ImageData data() {
                final String title = "Last second failures";
                final DatasetAdapter<LineChartGraphData> adapter = datasetAdapterFactory()
                        .forLineChart(title);
                final ImageData imageData = ImageData.noStatistics(title,
                        "Seconds", adapter);
                double max = 0;
                final Long[] keySet = failures().keySet().toArray(
                        new Long[failures().size()]);
                Arrays.sort(keySet);
                final long first = keySet[0];
                final long last = keySet[failures().size() - 1];
                for (long i = first; i <= last; i++) {
                    final FailedInvocations failedInvocations = failures().get(
                            i);
                    if (failedInvocations != null) {
                        final long failed = failedInvocations.failed();
                        if (failed > max) {
                            max = failed;
                        }
                        imageData.add(i, failed);
                    } else {
                        imageData.add(i, 0);
                    }
                }
                imageData.range(max + 10.00);
                return imageData;
            }

            @Override
            public String id() {
                return name + "-last-second-failures";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !failures().isEmpty();
            }
        };
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

}
