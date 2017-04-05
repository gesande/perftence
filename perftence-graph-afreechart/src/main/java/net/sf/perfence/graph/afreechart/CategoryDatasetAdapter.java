package net.sf.perfence.graph.afreechart;

import org.afree.data.Range;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.PaintType;

import net.sf.perftence.graph.DatasetAdapter;

public final class CategoryDatasetAdapter
		implements DatasetAdapter<BarChartGraphData, PaintType> {

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
	public BarChartGraphData graphData(final PaintType paint,
			final double range) {
		final BarChartGraphData data = new BarChartGraphData(legendTitle(),
				paint, dataSet());
		return data.range(new Range(0, range));
	}

}
