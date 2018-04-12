package org.fluentjava.perftence;

public final class DefaultLatencyProviderFactory implements LatencyProviderFactory {

    @Override
    public LatencyProvider newInstance() {
        return LatencyProvider.withSynchronized();
    }

}
