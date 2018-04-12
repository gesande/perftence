package org.fluentjava.perftence.graph.jfreechart;

import java.awt.Paint;

import org.fluentjava.perftence.graph.DatasetAdapter;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

final class CategoryDatasetAdapter implements DatasetAdapter<BarChartGraphData, Paint> {

    private final DefaultCategoryDataset categoryData;
    private final String legendTitle;

    public CategoryDatasetAdapter(final String legendTitle) {
        this.legendTitle = legendTitle;
        this.categoryData = new DefaultCategoryDataset();
    }

    private String legendTitle() {
        return this.legendTitle;
    }

    public CategoryDataset dataSet() {
        return this.categoryData;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void add(final Number x, final Number y) {
        this.categoryData.addValue(x, (Comparable) y, "");
    }

    @Override
    public BarChartGraphData graphData(final Paint paint, final double range) {
        final BarChartGraphData data = new BarChartGraphData(legendTitle(), paint, dataSet());
        return data.range(new Range(0, range));
    }

}
