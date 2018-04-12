package org.fluentjava.perftence;

public final class PerftenceRuntimeException extends RuntimeException {

    public PerftenceRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PerftenceRuntimeException(Throwable cause) {
        super(cause);
    }

    public PerftenceRuntimeException(String msg) {
        super(msg);
    }

}
