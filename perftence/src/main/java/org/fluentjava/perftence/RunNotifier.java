package org.fluentjava.perftence;

public interface RunNotifier {
    void finished(final String id);

    boolean isFinished(final String id);
}