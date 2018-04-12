package org.fluentjava.perftence;

public final class PerfTestFailure extends RuntimeException {

    PerfTestFailure(final String message) {
        super(message);
    }

}
