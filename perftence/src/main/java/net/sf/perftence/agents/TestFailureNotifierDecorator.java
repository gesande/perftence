package net.sf.perftence.agents;

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.TestFailureNotifier;

final class TestFailureNotifierDecorator implements TestFailureNotifier {
    private AtomicInteger failedTasks;
    private final TestFailureNotifier notifier;

    public TestFailureNotifierDecorator(final TestFailureNotifier notifier) {
        this.notifier = notifier;
        this.failedTasks = new AtomicInteger(0);
    }

    @Override
    public synchronized void testFailed(final Throwable t) {
        this.failedTasks.incrementAndGet();
        notifier().testFailed(t);
    }

    private TestFailureNotifier notifier() {
        return this.notifier;
    }

    public long failedTasks() {
        return this.failedTasks.longValue();
    }
}