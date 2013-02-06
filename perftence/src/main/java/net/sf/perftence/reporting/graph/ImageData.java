package net.sf.perftence.reporting.graph;

import net.sf.perftence.reporting.Statistics;

public final class ImageData {
    private final String title;
    private final String xAxisTitle;
    private double range;
    private final Statistics statistics;
    private final DatasetAdapter<?> adapter;

    private ImageData(final String title, final String xAxisTitle,
            final double range, final Statistics statistics,
            final DatasetAdapter<?> adapter) {
        this.title = title;
        this.xAxisTitle = xAxisTitle;
        this.range = range;
        this.statistics = statistics;
        this.adapter = adapter;
    }

    public static ImageData statistics(final String title,
            final String xAxisTitle, final double range,
            final Statistics statistics, final DatasetAdapter<?> adapter) {
        return new ImageData(title, xAxisTitle, range, statistics, adapter);
    }

    public static ImageData noStatistics(final String title,
            final String xAxisTitle, final double range,
            final DatasetAdapter<?> adapter) {
        return new ImageData(title, xAxisTitle, range, null, adapter);
    }

    public static ImageData noStatistics(final String title,
            final String xAxisTitle, final DatasetAdapter<?> adapter) {
        return new ImageData(title, xAxisTitle, 0, null, adapter);
    }

    public void add(final Number x, final Number y) {
        adapter().add(x, y);
    }

    public DatasetAdapter<?> adapter() {
        return this.adapter;
    }

    public String title() {
        return this.title;
    }

    public String xAxisLabel() {
        return this.xAxisTitle;
    }

    public double range() {
        return this.range;
    }

    public Statistics statistics() {
        return this.statistics;
    }

    public boolean hasStatistics() {
        return this.statistics != null;
    }

    public ImageData range(final double range) {
        this.range = range;
        return this;
    }

}
