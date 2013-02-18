package net.sf.perftence.common;

import net.sf.perftence.reporting.ReportingOptionsFactory;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;

public final class InvocationStorageFactory {
    private InvocationStorageFactory() {
    }

    public static InvocationStorage newDefaultInvocationStorage(
            final int invocations, final int invocationRange,
            DatasetAdapterFactory datasetAdapterFactory) {
        return DefaultInvocationStorage.newDefaultStorage(invocations,
                ReportingOptionsFactory
                        .latencyOptionsWithStatistics(invocationRange),
                datasetAdapterFactory);
    }

}
