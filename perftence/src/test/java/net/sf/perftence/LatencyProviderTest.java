package net.sf.perftence;

import org.junit.Test;

public class LatencyProviderTest {

    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public void tooHastyNotEventStarted() {
        LatencyProvider.withSynchronized().throughput();
    }

    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public void tooHastyStartedButNotStopped() {
        final LatencyProvider provider = LatencyProvider.withSynchronized();
        provider.start();
        provider.throughput();
    }

}
