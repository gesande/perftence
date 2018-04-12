package org.fluentjava.perftence.agents;

public interface RunnableAdapter {
    Runnable adapt(final TestTask task, final long timeItWasScheduledToBeRun);
}
