package net.sf.perftence.agents;

final class SchedulingServiceFactory {

    @SuppressWarnings("static-method")
    public TestTaskSchedulingService newBasedOnJavaConcurrentStuff(
            final RunnableAdapter adapter, final int workers,
            final ScheduledTasks scheduledTasks) {
        return new SchedulingServiceBasedOnJavaConcurrentStuff(adapter,
                workers, scheduledTasks);
    }

}
