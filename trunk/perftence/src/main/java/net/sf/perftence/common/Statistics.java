package net.sf.perftence.common;

import java.util.List;

import net.sf.perftence.graph.GraphStatisticsProvider;
import net.sf.perftence.statistics.StatisticsCalculator;

public final class Statistics implements GraphStatisticsProvider {

	private final StatisticsCalculator statistics;

	private Statistics(final StatisticsCalculator statistics) {
		this.statistics = statistics;
	}

	public int percentile90() {
		return statistics().percentileValue(90);
	}

	private StatisticsCalculator statistics() {
		return this.statistics;
	}

	/**
	 * Calculates the throughput (invocations / second) for given values.
	 * 
	 * @param elapsedTime
	 *            in milliseconds
	 * @return the throughput (invocations / second)
	 */

	@SuppressWarnings("static-method")
	public double throughput(final long elapsedTime,
			final long invocationCount) {
		return (invocationCount / (elapsedTime / 1000.00));
	}

	@Override
	public int median() {
		return statistics().median();
	}

	public int max() {
		return statistics().max();
	}

	public int min() {
		return statistics().min();
	}

	@Override
	public int percentile95() {
		return statistics().percentileValue(95);
	}

	public int percentile99() {
		return statistics().percentileValue(99);
	}

	@Override
	public double mean() {
		return statistics().mean();
	}

	public double standardDeviation() {
		return statistics().standardDeviation();
	}

	public double variance() {
		return statistics().variance();
	}

	public int percentile96() {
		return statistics().percentileValue(96);
	}

	public int percentile97() {
		return statistics().percentileValue(97);
	}

	public int percentile98() {
		return statistics().percentileValue(98);
	}

	public static Statistics fromLatencies(final List<Integer> latencies) {
		return new Statistics(StatisticsCalculator.fromValues(latencies));
	}

}