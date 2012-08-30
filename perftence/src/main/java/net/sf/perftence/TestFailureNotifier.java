package net.sf.perftence;

public interface TestFailureNotifier {
    public static final String FailureNotifierIsNullMessage = "Seems that failureNotifier is null for some reason....did you forget to add @RunsWith(DefaultTestRunner.class) to your test class / test abstraction.";

    public void testFailed(final Throwable t);

    public static final RuntimeException NOTIFIER_IS_NULL = new RuntimeException(
            FailureNotifierIsNullMessage);
}
