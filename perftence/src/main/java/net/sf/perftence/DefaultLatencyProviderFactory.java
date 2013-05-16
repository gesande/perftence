package net.sf.perftence;

public class DefaultLatencyProviderFactory implements LatencyProviderFactory {

    @Override
    public LatencyProvider newInstance() {
        return LatencyProvider.withSynchronized();
    }

}
