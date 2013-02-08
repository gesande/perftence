package net.sf.perftence.reporting;

public final class WritingFileFailed extends RuntimeException {

    public WritingFileFailed(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public WritingFileFailed(String msg) {
        super(msg);
    }
}