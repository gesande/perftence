package net.sf.perftence.statistics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StatisticsCalculatorTest {

	@Test
	public void stats() {
		final List<Integer> values = new ArrayList<>();
		for (int i = 100; i > -1; i--) {
			values.add(i);
		}
		StatisticsCalculator stat = StatisticsCalculator.fromValues(values);
		assertEquals("Min doesn't match!", 0, stat.min());
		assertEquals("Max doesn't match!", 100, stat.max());
		assertEquals("Mean doesn't match!", 50, stat.mean(), 0);
		assertEquals("Median doesn't match!", 50, stat.median(), 0);
		assertEquals("50 percentile doesn't match!", 50,
				stat.percentileValue(50), 0);
		assertEquals("90 percentile doesn't match!", 90,
				stat.percentileValue(90), 0);
		assertEquals("95 percentile doesn't match!", 95,
				stat.percentileValue(95), 0);
		assertEquals("96 percentile doesn't match!", 96,
				stat.percentileValue(96), 0);
		assertEquals("97 percentile doesn't match!", 97,
				stat.percentileValue(97), 0);
		assertEquals("98 percentile doesn't match!", 98,
				stat.percentileValue(98), 0);
		assertEquals("99 percentile doesn't match!", 99,
				stat.percentileValue(99), 0);
		assertEquals("Standard deviation doesn't match!", 29.154759474226502,
				stat.standardDeviation(), 0);
		assertEquals("Standard deviation doesn't match!", 850.0,
				stat.variance(), 0);
	}

	@Test
	public void statsEven() {
		final List<Integer> values = new ArrayList<>();
		for (int i = 100; i > 0; i--) {
			values.add(i);
		}
		StatisticsCalculator stat = StatisticsCalculator.fromValues(values);
		assertEquals("Min doesn't match!", 1, stat.min());
		assertEquals("Max doesn't match!", 100, stat.max());
		assertEquals("Mean doesn't match!", 50.5, stat.mean(), 0);
		assertEquals("Median doesn't match!", 50, stat.median(), 0);
		assertEquals("50 percentile doesn't match!", 50,
				stat.percentileValue(50), 0);
		assertEquals("90 percentile doesn't match!", 90,
				stat.percentileValue(90), 0);
		assertEquals("95 percentile doesn't match!", 95,
				stat.percentileValue(95), 0);
		assertEquals("96 percentile doesn't match!", 96,
				stat.percentileValue(96), 0);
		assertEquals("97 percentile doesn't match!", 97,
				stat.percentileValue(97), 0);
		assertEquals("98 percentile doesn't match!", 98,
				stat.percentileValue(98), 0);
		assertEquals("99 percentile doesn't match!", 99,
				stat.percentileValue(99), 0);
		assertEquals("Standard deviation doesn't match!", 28.86607004772212,
				stat.standardDeviation(), 0);
		assertEquals("Standard deviation doesn't match!", 833.25,
				stat.variance(), 0);
	}

	@Test
	public void mean() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("Mean doesn't match!", 394.00, stat.mean(), 0);
	}

	@Test
	public void median() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("Median doesn't match!", 430, stat.median(), 0);
	}

	@Test
	public void percentile90() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("90 percentile doesn't match!", 600,
				stat.percentileValue(90), 0);
	}

	@Test
	public void percentile95() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("95 percentile doesn't match!", 600,
				stat.percentileValue(95), 0);
	}

	@Test
	public void percentile96() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("96 percentile doesn't match!", 600,
				stat.percentileValue(96), 0);
	}

	@Test
	public void percentile97() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("97 percentile doesn't match!", 600,
				stat.percentileValue(97), 0);
	}

	@Test
	public void percentile98() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("97 percentile doesn't match!", 600,
				stat.percentileValue(98), 0);
	}

	@Test
	public void percentile99() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("99 percentile doesn't match!", 600,
				stat.percentileValue(99), 0);
	}

	@Test
	public void standardDeviation() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("Standard deviation doesn't match!", 147.32277488562318,
				stat.standardDeviation(), 0);
	}

	@Test
	public void variance() {
		StatisticsCalculator stat = StatisticsCalculator
				.fromValues(sampleList());
		assertEquals("Variance doesn't match!", 21704, stat.variance(), 0);
	}

	@Test
	public void empty() {
		final StatisticsCalculator empty = StatisticsCalculator
				.fromValues(new ArrayList<Integer>());
		assertEquals(0, empty.max());
		assertEquals(0, empty.min());
		assertEquals(0, empty.mean(), 0);
		assertEquals(0, empty.median());
		assertEquals(0, empty.percentileValue(90));
		assertEquals(0, empty.percentileValue(95));
		assertEquals(0, empty.percentileValue(96));
		assertEquals(0, empty.percentileValue(97));
		assertEquals(0, empty.percentileValue(98));
		assertEquals(0, empty.percentileValue(99));
		assertEquals(Double.NaN, empty.variance(), 0);
		assertEquals(Double.NaN, empty.standardDeviation(), 0);
	}

	private static List<Integer> sampleList() {
		final List<Integer> list = new ArrayList<>();
		list.add(600);
		list.add(470);
		list.add(170);
		list.add(430);
		list.add(300);
		return list;
	}
}
