package net.sf.perftence.agents;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScheduleTestTaskTest {

    @Test
    public void scheduleToFuture() throws InterruptedException {
        long now = nanoTime();
        final long scheduled = now;
        final ScheduledTestTask task = new ScheduledTestTask(task(inMillis(250)), scheduled);
        assertFalse(notTimeToRun(task, scheduled, now), task.isTimeToRun(now));
        sleep(1);
        now = nanoTime();
        assertFalse(notTimeToRun(task, scheduled, now), task.isTimeToRun(now));
        sleep(100);
        now = nanoTime();
        assertFalse(notTimeToRun(task, scheduled, now), task.isTimeToRun(now));
        sleep(100);
        now = nanoTime();
        assertFalse(notTimeToRun(task, scheduled, now), task.isTimeToRun(now));
        sleep(50);
        now = nanoTime();
        assertTrue(timeToRun(task, scheduled, now), task.isTimeToRun(now));
    }

    private static String timeToRun(final ScheduledTestTask task, final long scheduled, final long now) {
        return "It should have been time to run! Now =" + now + " scheduled time=" + scheduled + ", timeItWasScheduled="
                + task.timeItWasScheduledToBeRun();

    }

    private static String notTimeToRun(final ScheduledTestTask task, final long scheduled, final long now) {
        return "It should not have been time to run! Now =" + now + " scheduled time=" + scheduled
                + ", timeItWasScheduled=" + task.timeItWasScheduledToBeRun();
    }

    @Test
    public void scheduleNow() {
        final long now = nanoTime();
        assertTrue("It should have been time to run!", new ScheduledTestTask(task(inMillis(0)), now).isTimeToRun(now));
    }

    @Test
    public void scheduleToPast() {
        final long now = nanoTime();
        assertTrue("It should have been time to run!",
                new ScheduledTestTask(task(inMillis(-100)), now).isTimeToRun(now));
    }

    private static TestTask task(final Time when) {
        return new TestTask() {

            @Override
            public Time when() {
                return when;
            }

            @Override
            public void run(final TestTaskReporter reporter) throws Exception {
                // no implemementation
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

    private static Time inMillis(final long time) {
        return TimeSpecificationFactory.inMillis(time);
    }

    private static long nanoTime() {
        return System.nanoTime();
    }
}
