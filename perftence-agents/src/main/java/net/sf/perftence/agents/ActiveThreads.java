package net.sf.perftence.agents;

import java.util.concurrent.atomic.AtomicInteger;

final public class ActiveThreads {
	private final AtomicInteger threads = new AtomicInteger(0);

	public int more() {
		return threads().incrementAndGet();
	}

	public int less() {
		return threads().decrementAndGet();
	}

	public int active() {
		return threads().intValue();
	}

	public void reset() {
		threads().set(0);
	}

	private AtomicInteger threads() {
		return this.threads;
	}
}