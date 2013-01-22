package net.sf.perftence.agents;

import net.sf.perftence.TestFailureNotifier;

final class SchedulingServiceFactory {

    @SuppressWarnings("static-method")
    public TestTaskSchedulingService newBasedOnTaskProvider(
            final Time workerWaitTime, final RunnableAdapter adapter,
            final int workers, TestFailureNotifier testFailureNotifier) {
        return new SchedulingServiceBasedOnTaskProvider(new TaskProvider(
                workerWaitTime), adapter, workers, testFailureNotifier);
    }

    @SuppressWarnings("static-method")
    public TestTaskSchedulingService newBasedOnJavaConcurrentStuff(
            final RunnableAdapter adapter, final int workers,
            final ScheduledTasks scheduledTasks) {
        return new SchedulingServiceBasedOnJavaConcurrentStuff(adapter,
                workers, scheduledTasks);
    }

}
