package net.sf.perftence.reporting;

public final class InvocationStorageFactory {
    private InvocationStorageFactory() {
    }

    public static InvocationStorage newDefaultInvocationStorage(
            final int invocations, final int invocationRange) {
        return DefaultInvocationStorage.newDefaultStorage(invocations,
                ReportingOptionsFactory.latencyOptionsWithStatistics(invocationRange));
    }

}
