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

public final class LastSecondIntermediateStatisticsProvider implements
        CustomIntermediateSummaryProvider {
    private final AdjustedFieldBuilder fieldBuilder;
    private final RuntimeStatisticsProvider statistics;
    private List<Double> throughputs;

    public LastSecondIntermediateStatisticsProvider(
            final AdjustedFieldBuilder fieldBuilder,
            final RuntimeStatisticsProvider statistics) {
        this.fieldBuilder = fieldBuilder;
        this.statistics = statistics;
        this.throughputs = new ArrayList<Double>();
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
                for (int i = 0; i < throughputs.size(); i++) {
                    imageData.add(i + 1, throughputs.get(i));
                }
                return imageData;
            }

            @Override
            public String id() {
                return name;
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