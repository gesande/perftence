package net.sf.perftence;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runners.model.InitializationError;

public class DefaultTestRunnerTest {

	private static TestFailureNotifier failureNotifier;

	@SuppressWarnings("unused")
	@Test
	public void failureNotifierIsSet() throws InitializationError {
		new DefaultTestRunner(DefaultTestRunnerTest.class);
		assertNotNull(failureNotifier);
	}

	public static void failureNotifier(final TestFailureNotifier notifier) {
		DefaultTestRunnerTest.failureNotifier = notifier;
	}
}
