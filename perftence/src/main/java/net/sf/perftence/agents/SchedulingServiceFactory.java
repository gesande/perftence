package net.sf.perftence.agents;

import net.sf.perftence.TestFailureNotifier;

final class SchedulingServiceFactory {

    private SchedulingServiceFactory() {
    }

    public static TestTaskSchedulingService newBasedOnTaskProvider(
            final Time workerWaitTime, final RunnableAdapter adapter,
            final int workers, TestFailureNotifier testFailureNotifier) {
        return new SchedulingServiceBasedOnTaskProvider(new TaskProvider(
                workerWaitTime), adapter, workers, testFailureNotifier);
    }

    public static TestTaskSchedulingService newBasedOnJavaConcurrentStuff(
            final RunnableAdapter adapter, final int workers,
            final ScheduledTasks scheduledTasks) {
        return new SchedulingServiceBasedOnJavaConcurrentStuff(adapter,
                Integer.MAX_VALUE, workers, scheduledTasks);
    }

}
