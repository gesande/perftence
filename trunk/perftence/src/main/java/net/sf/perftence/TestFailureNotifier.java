package net.sf.perftence;

public interface TestFailureNotifier {
    final String FailureNotifierIsNullMessage = "Seems that failureNotifier is null for some reason....did you forget to add @RunsWith(DefaultTestRunner.class) to your test class / test abstraction.";

    public void testFailed(final Throwable t);

    final RuntimeException NOTIFIER_IS_NULL = new RuntimeException(
            FailureNotifierIsNullMessage);
}
