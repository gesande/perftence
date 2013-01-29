package net.sf.perftence.reporting.graph;

class WritingFileFailed extends RuntimeException {

    public WritingFileFailed(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}