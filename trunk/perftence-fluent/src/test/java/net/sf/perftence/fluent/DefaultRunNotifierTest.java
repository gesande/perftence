package net.sf.perftence.fluent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultRunNotifierTest {

	@SuppressWarnings("static-method")
	@Test
	public void isFinished() {
		DefaultRunNotifier notifier = new DefaultRunNotifier();
		final String id = "id";
		assertFalse(notifier.isFinished(id));
		notifier.finished(id);
		assertTrue(notifier.isFinished(id));
	}
}
