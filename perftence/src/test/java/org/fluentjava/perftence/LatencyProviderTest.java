package org.fluentjava.perftence;

import org.junit.Test;

public class LatencyProviderTest {

    @Test(expected = IllegalStateException.class)
    public void tooHastyNotEventStarted() {
        newLatencyProvider().throughput();
    }

    @Test(expected = IllegalStateException.class)
    public void tooHastyStartedButNotStopped() {
        final LatencyProvider provider = newLatencyProvider();
        provider.start();
        provider.throughput();
    }

    private static LatencyProvider newLatencyProvider() {
        return new DefaultLatencyProviderFactory().newInstance();
    }
}
