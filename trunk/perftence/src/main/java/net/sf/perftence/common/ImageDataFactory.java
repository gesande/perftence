package net.sf.perftence.common;

import net.sf.perftence.graph.DatasetAdapter;
import net.sf.perftence.graph.GraphStatisticsProvider;
import net.sf.perftence.graph.ImageData;
import net.sf.perftence.graph.jfreechart.DatasetAdapterFactory;
import net.sf.perftence.reporting.ReportingOptions;

final class ImageDataFactory {
    private final DatasetAdapterFactory datasetAdapterFactory;

    public ImageDataFactory(final DatasetAdapterFactory datasetAdapterFactory) {
        this.datasetAdapterFactory = datasetAdapterFactory;
    }

    public ImageData newImageDataForLineChart(
            final ReportingOptions reportingOptions,
            final GraphStatisticsProvider statistics) {
        return newImageData(
                datasetAdapterFactory().forLineChart(
                        reportingOptions.legendTitle()), reportingOptions,
                statistics);
    }

    private static ImageData newImageData(final DatasetAdapter<?> adapter,
            final ReportingOptions reportingOptions,
            final GraphStatisticsProvider statistics) {
        return newImageData(adapter, reportingOptions, statistics,
                reportingOptions.title(), reportingOptions.xAxisTitle(),
                reportingOptions.range());
    }

    private static ImageData newImageData(final DatasetAdapter<?> adapter,
            final ReportingOptions reportingOptions,
            final GraphStatisticsProvider statistics, final String title,
            final String xAxisTitle, final int range) {
        return reportingOptions.provideStatistics() ? ImageData.statistics(
                title, xAxisTitle, range, statistics, adapter) : ImageData
                .noStatistics(title, xAxisTitle, range, adapter);
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }
}