package net.sf.perftence.reporting.summary;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.reporting.graph.DatasetAdapter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.graph.ImageData;
import net.sf.perftence.reporting.graph.ImageFactory;
import net.sf.perftence.reporting.graph.LineChartGraphData;

import org.apache.commons.collections.list.SynchronizedList;

public final class LastSecondThroughput implements ValueReporter<Double> {
    private List<Double> throughputs;
    private final DatasetAdapterFactory datasetAdapterFactory;

    @SuppressWarnings("unchecked")
    public LastSecondThroughput(
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.datasetAdapterFactory = datasetAdapterFactory;
        this.throughputs = SynchronizedList.decorate(new ArrayList<Double>());
    }

    private List<Double> throughputs() {
        return this.throughputs;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    public GraphWriter throughputGraphWriter(final String name) {
        return new GraphWriter() {

            @Override
            public void writeImage(final ImageFactory imageFactory) {
                imageFactory.createXYLineChart(id(), throughputData());
            }

            private ImageData throughputData() {
                final String title = "Last second throughput";
                final DatasetAdapter<LineChartGraphData> adapter = datasetAdapterFactory()
                        .forLineChart(title);
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

    @Override
    public void report(final Double value) {
        throughputs().add(value);
    }
}
