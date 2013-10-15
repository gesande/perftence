package net.sf.perftence;

import org.junit.Test;

public class LatencyProviderTest {

	@SuppressWarnings("static-method")
	@Test(expected = IllegalStateException.class)
	public void tooHastyNotEventStarted() {
		newLatencyProvider().throughput();
	}

	@SuppressWarnings("static-method")
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
