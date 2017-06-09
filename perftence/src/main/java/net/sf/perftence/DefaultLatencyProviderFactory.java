package net.sf.perftence;

public final class DefaultLatencyProviderFactory implements LatencyProviderFactory {

    @Override
    public LatencyProvider newInstance() {
        return LatencyProvider.withSynchronized();
    }

}
