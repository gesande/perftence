package net.sf.perftence.fluent;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InvocationRunnerFactory {
    private static final Logger LOG = LoggerFactory
            .getLogger(InvocationRunnerFactory.class);

    private InvocationRunnerFactory() {
    }

    private static Logger log() {
        return LOG;
    }

    public static InvocationRunner create(final RunNotifier runNotifier,
            final TestFailureNotifier failureNotifier, final String id) {
        return new InvocationRunner() {
            /**
             * Executing threads
             */
            private Thread threads[] = null;

            private Thread[] threads() {
                return this.threads;
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public void run(final Invocation[] runnables) {
                validate(runnables);
                createThreads(runnables);
                startThreads();
                joinThreads();
                clearThreads();
            }

            private void validate(final Invocation[] runnables) {
                if (runnables == null) {
                    throw new IllegalArgumentException("runnables is null");
                }
            }

            private void clearThreads() {
                this.threads = null;
                log().debug("Test threads 'cleared'.");
            }

            private void joinThreads() {
                try {
                    for (int i = 0; i < threads().length; i++) {
                        threads()[i].join();
                    }
                } catch (InterruptedException ignore) {
                    log().warn("Thread join interrupted");
                }
            }

            private void startThreads() {
                log().debug("Starting test threads...");
                for (int i = 0; i < threads().length; i++) {
                    threads()[i].start();
                }
                log().debug("Test threads started.");
            }

            private void createThreads(final Invocation[] runnables) {
                this.threads = new Thread[runnables.length];
                for (int i = 0; i < threads().length; i++) {
                    threads()[i] = new Thread(runnables[i],
                            performanceTestThread(i));
                }
            }

            private String performanceTestThread(final int index) {
                return new StringBuffer("perf-test-").append(index).toString();
            }

            @Override
            public void interruptThreads() {
                log().debug("Interrupting test threads...");
                for (int i = 0; i < threads().length; i++) {
                    threads()[i].interrupt();
                }
                log().debug("Test threads interrupted.");
            }

            @Override
            public void testFailed(Throwable t) {
                failureNotifier.testFailed(t);
            }

            @Override
            public void finished(final String id) {
                log().debug("Reporting {} finished.", id);
                runNotifier.finished(id);
            }

            @Override
            public boolean isFinished(final String id) {
                return runNotifier.isFinished(id);
            }
        };
    }
}
