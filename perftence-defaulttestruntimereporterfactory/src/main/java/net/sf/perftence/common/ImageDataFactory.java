package net.sf.perftence.common;

import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.GraphStatisticsProvider;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.reporting.ReportingOptions;

public final class ImageDataFactory {
	private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;

	public ImageDataFactory(
			final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
		this.lineChartAdapterProvider = lineChartAdapterProvider;
	}

	public ImageData newImageDataForLineChart(
			final ReportingOptions reportingOptions,
			final GraphStatisticsProvider statistics) {
		return newImageData(linechartAdapter(reportingOptions.legendTitle()),
				reportingOptions, statistics);
	}

	public ImageData newImageDataForLineChart(final String title,
			final String xAxisTitle) {
		return ImageData.noStatistics(title, xAxisTitle,
				linechartAdapter(title));
	}

	private DatasetAdapter<?, ?> linechartAdapter(final String legendTitle) {
		return lineChartAdapterProvider().forLineChart(legendTitle);
	}

	private static ImageData newImageData(final DatasetAdapter<?, ?> adapter,
			final ReportingOptions reportingOptions,
			final GraphStatisticsProvider statistics) {
		return newImageData(adapter, reportingOptions, statistics,
				reportingOptions.title(), reportingOptions.xAxisTitle(),
				reportingOptions.range());
	}

	private static ImageData newImageData(final DatasetAdapter<?, ?> adapter,
			final ReportingOptions reportingOptions,
			final GraphStatisticsProvider statistics, final String title,
			final String xAxisTitle, final int range) {
		return reportingOptions.provideStatistics()
				? ImageData.statistics(title, xAxisTitle, range, statistics,
						adapter)
				: ImageData.noStatistics(title, xAxisTitle, range, adapter);
	}

	private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
		return this.lineChartAdapterProvider;
	}
}