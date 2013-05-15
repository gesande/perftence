package net.sf.perftence.common;

import net.sf.perftence.graph.jfreechart.DatasetAdapterFactory;

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
