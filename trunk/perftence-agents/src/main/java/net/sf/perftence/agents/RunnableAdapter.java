package net.sf.perftence.agents;

public interface RunnableAdapter {
    Runnable adapt(final TestTask task, final long timeItWasScheduledToBeRun);
}
