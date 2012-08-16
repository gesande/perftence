package net.sf.perftence.agents;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import net.sf.perftence.TestFailureNotifier;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskProviderTest {
    private final static Logger LOG = LoggerFactory
            .getLogger(TaskProviderTest.class);

    private AtomicInteger tasksRun;

    @Before
    public void before() {
        this.tasksRun = new AtomicInteger();
    }

    @Test
    public void schedule1000TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 1000);
    }

    @Test
    public void schedule5000TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 5000);
    }

    @Test
    public void scheduleTaskWithNextTasks() throws Exception {
        runTasksWithNextTasks(newTaskProvider(), 1, 2500);
    }

    @Test
    public void schedule1000TaskWithNextTasks() throws Exception {
        runTasksWithNextTasks(newTaskProvider(), 1000, 5000);
    }

    @Test
    public void schedule10000TaskWithNextTasks() throws Exception {
        runTasksWithNextTasks(newTaskProvider(), 10000, 5000);
    }

    private void runTasksWithNextTasks(final TaskProvider taskProvider,
            final int tasks, int sleepUntilUglyStop)
            throws InterruptedException, ScheduleFailedException {
        startingTest(tasks);
        final List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(1000, sleepingTask(1000)));
            final Thread t = newThread(taskProvider, nameForWorkerThread(i));
            threads.add(t);
            t.start();
        }
        sleepingUntilUglyStop(sleepUntilUglyStop);
        interruptAndJoin(threads);
        assertForNextTasks(tasks);
        done();
    }

    private void assertForNextTasks(final int tasks) {
        assertEquals("All tasks were not run!", tasks * 2,
                this.tasksRun.intValue());
    }

    private static void interruptAndJoin(final List<Thread> threads)
            throws InterruptedException {
        log().debug("All work done, interrupting the threads");
        for (final Thread t : threads) {
            t.interrupt();
        }
        log().debug("Join.");
        for (final Thread t : threads) {
            t.join();
        }
    }

    private static void sleepingUntilUglyStop(final int sleep)
            throws InterruptedException {
        log().debug("sleep until ugly stop");
        Thread.sleep(sleep);
        log().debug("Temporary ugly stop");
    }

    private static void startingTest(final int tasks) {
        log().debug(
                "Going to create and start {} worker threads for running the tasks with next tasks.",
                tasks);
    }

    private void done() {
        log().debug("Done with {} tasks.", this.tasksRun.intValue());
    }

    private static TaskProvider newTaskProvider() {
        return new TaskProvider(inMillis(1000));
    }

    private static Time inMillis(final long time) {
        return new Time() {

            @Override
            public TimeUnit timeUnit() {
                return TimeUnit.MILLISECONDS;
            }

            @Override
            public long time() {
                return time;
            }
        };
    }

    private void runTasks(final TaskProvider taskProvider, final int tasks)
            throws InterruptedException, ScheduleFailedException {
        log().debug(
                "Going to create and start {} worker threads for running the tasks.",
                tasks);
        final List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(1000));
            final Thread t = newThread(taskProvider, nameForWorkerThread(i));
            threads.add(t);
            t.start();
        }
        log().debug("sleep until ugly stop");
        Thread.sleep(3000);
        log().debug("Temporary ugly stop");
        interruptAndJoin(threads);
        assertEquals("All tasks were not run!", tasks, this.tasksRun.intValue());
        done();
    }

    private static void schedule(final TaskProvider taskProvider,
            final TestTask task) throws ScheduleFailedException {
        taskProvider.schedule(task);
    }

    private static Worker newWorker(final TaskProvider taskProvider) {
        return new Worker(taskProvider, new RunnableAdapter() {

            @Override
            public Runnable adapt(final TestTask task, long timeItWasScheduled) {
                return new Runnable() {

                    @Override
                    public void run() {
                        try {
                            task.run(null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        }, new TestFailureNotifier() {
            @Override
            public void testFailed(Throwable t) {
                Assert.fail("Test fails " + t.getMessage());
            }
        });
    }

    private TestTask sleepingTask(final int sleep) {
        return sleepingTask(sleep, null, TimeSpecificationFactory.now());
    }

    private TestTask sleepingTask(final int sleep, final TestTask nextTask) {
        return sleepingTask(sleep, nextTask, TimeSpecificationFactory.now());
    }

    private TestTask sleepingTask(final int sleep, final TestTask nextTask,
            final Time when) {
        return new TestTask() {

            @Override
            public Time when() {
                return when;
            }

            @Override
            public void run(final TestTaskReporter reporter) throws Exception {
                log().debug("Sleeping...");
                Thread.sleep(sleep);
                log().debug("Done");
                TaskProviderTest.this.tasksRun.incrementAndGet();
            }

            @Override
            public TestTask nextTaskIfAny() {
                return nextTask;
            }

            @Override
            public TestTaskCategory category() {
                return Category.One;
            }
        };
    }

    enum Category implements TestTaskCategory {
        One
    }

    private static Logger log() {
        return LOG;
    }

    private static Thread newThread(final TaskProvider taskProvider, String name) {
        return new Thread(newWorker(taskProvider), name);
    }

    private static String nameForWorkerThread(final int i) {
        return "thread-worker-" + i;
    }

}
