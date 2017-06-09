package net.sf.perftence.agents;

import java.util.concurrent.ScheduledThreadPoolExecutor;

final class SchedulingServiceFactory {

    @SuppressWarnings("static-method")
    public TestTaskSchedulingService newSchedulingService(final RunnableAdapter adapter, final int workers,
            final ScheduledTasks scheduledTasks) {
        return new SchedulingServiceBasedOnJavaConcurrentStuff(adapter, scheduledTasks,
                new ScheduledThreadPoolExecutor(workers));
    }

}
