package org.fluentjava.perftence.setup;

final class NoTestSetupDefined extends RuntimeException {
    public NoTestSetupDefined(final String msg) {
        super(msg);
    }
}