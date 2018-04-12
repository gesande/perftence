package org.fluentjava.perftence.common;

import org.fluentjava.perftence.graph.LineChartAdapterProvider;

public final class ThroughputStorageFactory {

    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;

    public ThroughputStorageFactory(final LineChartAdapterProvider<?, ?> lineChartAdapterProvider) {
        this.lineChartAdapterProvider = lineChartAdapterProvider;
    }

    public ThroughputStorage forRange(final int range) {
        return new DefaultThroughputStorage(range, adapterProvider());
    }

    private LineChartAdapterProvider<?, ?> adapterProvider() {
        return this.lineChartAdapterProvider;
    }
}
