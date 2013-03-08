package net.sf.perftence.fluent;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.concurrent.ThreadEngineApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InvocationRunnerFactory {
    private final static Logger LOG = LoggerFactory
            .getLogger(InvocationRunnerFactory.class);
    private final ThreadEngineApi<Invocation> engineApi;

    public InvocationRunnerFactory(final ThreadEngineApi<Invocation> engineApi) {
        this.engineApi = engineApi;
    }

    public InvocationRunner create(final RunNotifier runNotifier,
            final TestFailureNotifier failureNotifier, final String id) {
        return new InvocationRunner() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public void run(final Invocation[] runnables) {
                engineApi().runnables(runnables).run();
            }

            @Override
            public void interruptThreads() {
                engineApi().interrupt();
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

    private ThreadEngineApi<Invocation> engineApi() {
        return InvocationRunnerFactory.this.engineApi;
    }

    private static Logger log() {
        return LOG;
    }
}
