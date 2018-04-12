package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.GraphData;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;

final class BarChartGraphData implements GraphData<BarChartGraphData, Range, Paint> {

    private final String title;
    private final Paint paint;
    private final CategoryDataset categoryDataset;
    private Range range;

    BarChartGraphData(final String title, final Paint paint, final CategoryDataset categoryDataset) {
        this.title = title;
        this.paint = paint;
        this.categoryDataset = categoryDataset;
    }

    public CategoryDataset categoryDataset() {
        return this.categoryDataset;
    }

    @Override
    public Paint paint() {
        return this.paint;
    }

    @Override
    public BarChartGraphData range(Range range) {
        this.range = range;
        return this;
    }

    @Override
    public Range range() {
        return this.range;
    }

    @Override
    public String title() {
        return this.title;
    }

}