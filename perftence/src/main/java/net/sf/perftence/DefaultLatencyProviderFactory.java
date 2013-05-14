package net.sf.perftence;

public class DefaultLatencyProviderFactory implements LatencyProviderFactory {

    @SuppressWarnings("deprecation")
    @Override
    public LatencyProvider newInstance() {
        return LatencyProvider.withSynchronized();
    }

}
