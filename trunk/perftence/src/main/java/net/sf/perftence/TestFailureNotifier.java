package net.sf.perftence;

public interface TestFailureNotifier {

    public void testFailed(final Throwable t);

    final NoTestNotifierException NOTIFIER_IS_NULL = new NoTestNotifierException(
            "Seems that failureNotifier is null for some reason....did you forget to add @RunsWith(DefaultTestRunner.class) to your test class / test abstraction.");

    class NoTestNotifierException extends RuntimeException {
        public NoTestNotifierException(final String msg) {
            super(msg);
        }
    }
}
