package net.sf.perftence.reporting.summary;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.RuntimeStatisticsProvider;
import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.LineChartGraphData;

import org.apache.commons.collections.list.SynchronizedList;

public final class LastSecondIntermediateStatisticsProvider implements
        CustomIntermediateSummaryProvider {
    private final AdjustedFieldBuilder fieldBuilder;
    private final RuntimeStatisticsProvider statistics;
    private List<Double> throughputs;

    @SuppressWarnings("unchecked")
    public LastSecondIntermediateStatisticsProvider(
            final AdjustedFieldBuilder fieldBuilder,
            final RuntimeStatisticsProvider statistics) {
        this.fieldBuilder = fieldBuilder;
        this.statistics = statistics;
        this.throughputs = SynchronizedList.decorate(new ArrayList<Double>());
    }

    public GraphWriter throughputGraphWriter(final String name) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), throughputData());
            }

            private ImageData throughputData() {
                final String title = "Last second throughput";
                final DatasetAdapter<LineChartGraphData> adapter = DatasetAdapterFactory
                        .adapterForLineChart(title);
                final ImageData imageData = ImageData.noStatistics(title,
                        "Seconds", "Throughput", adapter);
                final List<Double> throughputs = throughputs();
                double max = 0;
                for (int i = 0; i < throughputs.size(); i++) {
                    final Double y = throughputs.get(i);
                    imageData.add((i + 1) * 1.00, y);
                    if (y > max) {
                        max = y;
                    }
                }
                imageData.range(max + 10.00);
                return imageData;
            }

            @Override
            public String id() {
                return name + "-last-second-throughput";
            }

            @Override
            public boolean hasSomethingToWrite() {
                return !throughputs().isEmpty();
            }
        };
    }

    private List<Double> throughputs() {
        return this.throughputs;
    }

    @Override
    public void provideIntermediateSummary(final IntermediateSummary summary) {
        summary.text("Last second stats:").endOfLine();
        summary.field(fieldBuilder().field("samples:",
                statistics().sampleCount()));
        summary.field(fieldBuilder().field("max:", statistics().maxLatency()));
        summary.field(fieldBuilder().field("average:",
                statistics().averageLatency()).asFormatted());
        summary.field(fieldBuilder().field("median:", statistics().median()));
        summary.field(fieldBuilder().field("95 percentile:",
                statistics().percentileLatency(95)));
        double currentThroughput = statistics().currentThroughput();
        throughputs().add(currentThroughput);
        summary.field(fieldBuilder().field("throughput:", currentThroughput)
                .asFormatted());
    }

    private RuntimeStatisticsProvider statistics() {
        return this.statistics;
    }

    private AdjustedFieldBuilder fieldBuilder() {
        return this.fieldBuilder;
    }
}