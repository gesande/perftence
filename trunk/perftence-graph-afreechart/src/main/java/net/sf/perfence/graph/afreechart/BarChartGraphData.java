package net.sf.perfence.graph.afreechart;

import net.sf.perftence.graph.GraphData;

import org.afree.data.Range;
import org.afree.data.category.CategoryDataset;
import org.afree.graphics.PaintType;

final class BarChartGraphData implements
		GraphData<BarChartGraphData, Range, PaintType> {

	private final String title;
	private final PaintType paintType;
	private final CategoryDataset categoryDataset;
	private Range range;

	BarChartGraphData(final String title, final PaintType paintType,
			final CategoryDataset categoryDataset) {
		this.title = title;
		this.paintType = paintType;
		this.categoryDataset = categoryDataset;
	}

	public CategoryDataset categoryDataset() {
		return this.categoryDataset;
	}

	@Override
	public PaintType paint() {
		return this.paintType;
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