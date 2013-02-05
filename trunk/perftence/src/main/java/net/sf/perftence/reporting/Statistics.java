package net.sf.perftence.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Statistics {

    private List<Integer> latencies;
    private List<Integer> sortedLatencies;

    private Statistics(final List<Integer> latencies,
            final List<Integer> sortedLatencies) {
        this.latencies = latencies;
        this.sortedLatencies = sortedLatencies;
    }

    public int percentile90() {
        return percentileValue(90);
    }

    /**
     * Calculates the throughput (invocations / second) for given values.
     * 
     * @param elapsedTime
     *            in milliseconds
     * @return the throughput (invocations / second)
     */
    @SuppressWarnings("static-method")
    public double throughput(final long elapsedTime, final long invocationCount) {
        return (invocationCount / (elapsedTime / 1000.00));
    }

    public int median() {
        return this.sortedLatencies.isEmpty() ? 0 : calculateMedian();
    }

    public int max() {
        return this.sortedLatencies.isEmpty() ? 0 : this.sortedLatencies
                .get(this.sortedLatencies.size() - 1);
    }

    public int min() {
        return this.sortedLatencies.isEmpty() ? 0 : this.sortedLatencies.get(0);
    }

    private Integer calculateMedian() {
        Integer result = 0;
        final int size = this.sortedLatencies.size();
        if (size % 2 == 1) { // If the number of entries in the list is not
                             // even.
            result = this.sortedLatencies.get(size / 2); // Get the middle
                                                         // value.
        } else { // If the number of entries in the list are even.
            final Integer lowerMiddle = this.sortedLatencies.get(size / 2);
            final Integer upperMiddle = this.sortedLatencies.get(size / 2 - 1);
            // Get the middle two values and average them.
            result = (lowerMiddle + upperMiddle) / 2;
        }
        return result;
    }

    public int percentile95() {
        return percentileValue(95);
    }

    private int percentileValue(final int percentileValue) {
        return this.sortedLatencies.isEmpty() ? 0 : this.sortedLatencies
                .get(percentile(percentileValue) - 1);
    }

    public int percentile99() {
        return percentileValue(99);
    }

    private int percentile(final int percentile) {
        final double i = percentile / 100.00 * this.sortedLatencies.size()
                + 0.5;
        return (int) (i);
    }

    public double mean() {
        if (this.latencies.size() == 0) {
            return 0;
        }
        long sum = 0;
        for (final Integer latency : this.latencies) {
            sum += latency;
        }
        return (double) sum / this.latencies.size();
    }

    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    public double variance() {
        long n = 0;
        double mean = 0;
        double s = 0.0;

        for (Integer x : this.latencies) {
            n++;
            final double delta = x - mean;
            mean += delta / n;
            s += delta * (x - mean);
        }
        // if you want to calculate std deviation
        // of a sample change this to (s/(n-1))
        return (s / n);
    }

    public int percentile96() {
        return percentileValue(96);
    }

    public int percentile97() {
        return percentileValue(97);
    }

    public int percentile98() {
        return percentileValue(98);
    }

    public static Statistics fromLatencies(final List<Integer> latencies) {
        return new Statistics(latencies, sortedLatencies(latencies));
    }

    private static List<Integer> sortedLatencies(List<Integer> latencies) {
        final List<Integer> sortedLatencies = new ArrayList<Integer>(latencies);
        Collections.sort(sortedLatencies);
        return sortedLatencies;
    }
}