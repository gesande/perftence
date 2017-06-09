package net.sf.perftence.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StatisticsCalculator {

    private final List<Integer> values;

    private StatisticsCalculator(final List<Integer> values) {
        this.values = values;
    }

    public static StatisticsCalculator fromValues(final List<Integer> values) {
        return new StatisticsCalculator(asSorted(values));
    }

    private static List<Integer> asSorted(List<Integer> latencies) {
        final List<Integer> sortedLatencies = new ArrayList<>(latencies);
        Collections.sort(sortedLatencies);
        return sortedLatencies;
    }

    public int percentileValue(final int percentileValue) {
        return this.values.isEmpty() ? 0 : this.values.get(percentile(percentileValue) - 1);
    }

    private int percentile(final int percentile) {
        final double i = percentile / 100.00 * this.values.size() + 0.5;
        return (int) (i);
    }

    public int median() {
        return this.values.isEmpty() ? 0 : calculateMedian();
    }

    private Integer calculateMedian() {
        Integer result = 0;
        final int size = this.values.size();
        if (size % 2 == 1) { // If the number of entries in the list is not
                             // even.
            result = this.values.get(size / 2); // Get the middle
                                                // value.
        } else { // If the number of entries in the list are even.
            final Integer lowerMiddle = this.values.get(size / 2);
            final Integer upperMiddle = this.values.get(size / 2 - 1);
            // Get the middle two values and average them.
            result = (lowerMiddle + upperMiddle) / 2;
        }
        return result;
    }

    public double mean() {
        if (this.values.size() == 0) {
            return 0;
        }
        long sum = 0;
        for (final Integer latency : this.values) {
            sum += latency;
        }
        return (double) sum / this.values.size();
    }

    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    public double variance() {
        long n = 0;
        double mean = 0;
        double s = 0.0;

        for (Integer x : this.values) {
            n++;
            final double delta = x - mean;
            mean += delta / n;
            s += delta * (x - mean);
        }
        // if you want to calculate std deviation
        // of a sample change this to (s/(n-1))
        return (s / n);
    }

    public int max() {
        return this.values.isEmpty() ? 0 : this.values.get(this.values.size() - 1);
    }

    public int min() {
        return this.values.isEmpty() ? 0 : this.values.get(0);
    }

}
