package net.sf.perftence.graph;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.AsSynchronized;
import net.sf.perftence.reporting.summary.ValueReporter;

public final class LastSecondThroughput implements ValueReporter<Double>,
		GraphWriterProvider {
	private final List<Double> throughputs;
	private final LineChartAdapterProvider<?, ?> linecharAdapterProvider;

	public LastSecondThroughput(
			final LineChartAdapterProvider<?, ?> linecharAdapterProvider) {
		this.linecharAdapterProvider = linecharAdapterProvider;
		this.throughputs = AsSynchronized.list(new ArrayList<Double>());
	}

	private List<Double> throughputs() {
		return this.throughputs;
	}

	@Override
	public GraphWriter graphWriterFor(final String id) {
		return new GraphWriter() {

			@Override
			public void writeImage(final ImageFactory imageFactory) {
				imageFactory.createXYLineChart(id(), throughputData());
			}

			private ImageData throughputData() {
				final String title = "Last second throughput";
				final ImageData imageData = ImageData.noStatistics(title,
						"Seconds",
						lineChartAdapterProvider().forLineChart(title));
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
				return id + "-last-second-throughput";
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

	private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
		return this.linecharAdapterProvider;
	}
}
