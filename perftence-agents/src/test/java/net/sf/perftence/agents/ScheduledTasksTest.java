package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScheduledTasksTest {

    @Test
    public void scheduleTask() {
        ScheduledTasks scheduled = new ScheduledTasks();
        assertEquals("", 0, scheduled.scheduledTasks());
        assertFalse("", scheduled.hasScheduledTasks());
        assertEquals("", null, scheduled.lastTaskScheduledToBeRun());
        scheduled.add(newTask(500));

        assertEquals("", 1, scheduled.scheduledTasks());
        assertTrue("", scheduled.hasScheduledTasks());
        final long time = scheduled.lastTaskScheduledToBeRun().time();
        assertTrue("", time > 0 && time <= 500);
    }

    private static TestTask newTask(final int when) {
        return new TestTask() {

            @Override
            public Time when() {
                return TimeSpecificationFactory.inMillis(when);
            }

            @Override
            public void run(final TestTaskReporter reporter) throws Exception {
                // no implementation
            }

            @Override
            public TestTask nextTaskIfAny() {
                return null;
            }

            @Override
            public TestTaskCategory category() {
                return null;
            }
        };
    }
}
