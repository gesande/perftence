package net.sf.perftence.agents;

import java.util.Collection;

public interface TestTaskSchedulingService {

    void scheduleTask(final TestTask task) throws ScheduleFailedException;

    void shutdown();

    void handleFailure(final TestTask task, Throwable cause);

    void markDone(TestTask task);

    void scheduleFirstTasks(final Collection<TestAgent> agents)
            throws ScheduleFailedException;

    void run();

}
