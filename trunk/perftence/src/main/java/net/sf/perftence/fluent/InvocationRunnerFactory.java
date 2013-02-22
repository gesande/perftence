package net.sf.perftence.fluent;

import java.util.concurrent.ThreadFactory;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.concurrent.NamedThreadFactory;
import net.sf.perftence.concurrent.ThreadEngine;

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
        final ThreadFactory threadFactory = NamedThreadFactory
                .forNamePrefix("perf-test-");
        final ThreadEngine engine = new ThreadEngine(threadFactory);
        return new InvocationRunner() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public void run(final Invocation[] runnables) {
                engine.run(runnables);
            }

            @Override
            public void interruptThreads() {
                engine.interruptThreads();
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
