package net.sf.perftence.reporting;

import net.sf.perftence.reporting.graph.DatasetAdapterFactory;

public final class ThroughputStorageFactory {

    private final DatasetAdapterFactory datasetAdapterFactory;

    public ThroughputStorageFactory(
            final DatasetAdapterFactory datasetAdapterFactory) {
        this.datasetAdapterFactory = datasetAdapterFactory;

    }

    public ThroughputStorage forRange(final int range) {
        return new DefaultThroughputStorage(range, datasetAdapterFactory());
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }
}
