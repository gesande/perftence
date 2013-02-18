package net.sf.perftence.setup;

final class NoTestSetupDefined extends RuntimeException {
    public NoTestSetupDefined(final String msg) {
        super(msg);
    }
}