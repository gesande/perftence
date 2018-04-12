package org.fluentjava.perftence;

public final class PerfTestFailureFactory {

    @SuppressWarnings("static-method")
    public RuntimeException newPerfTestFailure(final String message) {
        return new PerfTestFailure(message);
    }
}