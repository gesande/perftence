package net.sf.völundr.fileio;

public final class WritingFileFailed extends Exception {

    public WritingFileFailed(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public WritingFileFailed(String msg) {
        super(msg);
    }
}