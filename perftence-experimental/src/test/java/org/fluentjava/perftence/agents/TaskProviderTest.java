package org.fluentjava.perftence.agents;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.fluentjava.perftence.TestFailureNotifier;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;
import net.sf.völundr.concurrent.NamedThreadFactory;

public class TaskProviderTest {
    private final static Logger LOG = LoggerFactory.getLogger(TaskProviderTest.class);

    private AtomicInteger tasksRun;

    @Before
    public void before() {
        this.tasksRun = new AtomicInteger();
    }

    @Test
    public void addTasks() throws ScheduleFailedException {
        final TaskProvider taskProvider = newTaskProvider();
        final int tasks = 5;
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(100));
        }
        assertEquals(tasks, taskProvider.scheduledTasks());
    }

    @Test
    public void scheduleOrder() throws ScheduleFailedException, InterruptedException {
        final int sleepValue = 100;
        final TestTask first = sleepingTask(sleepValue, null, inMillis(250));
        final TestTask second = sleepingTask(sleepValue, null, inMillis(200));
        final TaskProvider newTaskProvider = newTaskProvider();
        log().debug("schedule first task");
        schedule(newTaskProvider, first);
        log().debug("schedule second task");
        schedule(newTaskProvider, second);
        final List<Thread> threads = new ArrayList<>();
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("worker-");
        final Thread newThread = threadFactory.newThread(newWorker(newTaskProvider));
        threads.add(newThread);
        log().debug("start worker");
        newThread.start();
        sleepingUntilUglyStop(1500);
        interruptAndJoin(threads);
        done();
    }

    @Test
    public void runOrder() throws ScheduleFailedException, InterruptedException {
        final long sleep = 100;
        final TestTask task2 = new TestTask() {

            @Override
            public Time when() {
                return TimeSpecificationFactory.now();
            }

            @Override
            public void run(TestTaskReporter reporter) throws Exception {
                log().debug("runnning task2");
                assertEquals("Task1 wasn't run before task2!", 1, TaskProviderTest.this.tasksRun.intValue());
                TaskProviderTest.this.tasksRun.incrementAndGet();
                log().debug("task2 done");
            }

            @Override
            public TestTask nextTaskIfAny() {
                return null;
            }

            @Override
            public TestTaskCategory category() {
                return Category.One;
            }
        };
        TestTask first = new TestTask() {

            @Override
            public Time when() {
                log().debug("when called");
                return inMillis(1000);
            }

            @Override
            public void run(TestTaskReporter reporter) throws Exception {
                log().debug("runnning task1");
                assertEquals("This should have been the first task to be run!", 0,
                        TaskProviderTest.this.tasksRun.intValue());
                log().debug("Sleeping...");
                sleep(sleep);
                TaskProviderTest.this.tasksRun.incrementAndGet();
                log().debug("task 1 done");
            }

            @Override
            public TestTask nextTaskIfAny() {
                log().debug("nextTaskIfAny called");
                return task2;
            }

            @Override
            public TestTaskCategory category() {
                return Category.One;
            }
        };
        final TaskProvider taskProvider = newTaskProvider();
        log().debug("first task scheduled.");
        taskProvider.schedule(first);
        final List<Thread> threads = new ArrayList<>();
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("thread-worker-");
        final Thread t = threadFactory.newThread(newWorker(taskProvider));
        threads.add(t);
        t.start();
        sleepingUntilUglyStop(1500);
        interruptAndJoin(threads);
        assertForNextTasks(1);
        done();
    }

    @Test
    public void schedule1TaskWithFutureTask() throws ScheduleFailedException, InterruptedException {
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("thread-worker-");
        final int tasks = 1;
        final TaskProvider taskProvider = newTaskProvider();
        startingTest(tasks);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(100, sleepingTask(100), inMillis(1000)));
            final Thread t = threadFactory.newThread(newWorker(taskProvider));
            threads.add(t);
            t.start();
        }
        sleepingUntilUglyStop(2000);
        interruptAndJoin(threads);
        assertForNextTasks(tasks);
        done();
    }

    @Test
    public void schedule1TaskToBeRun() throws Exception {
        runTasks(newTaskProvider(), 1, 2000);
    }

    @Test
    public void schedule2TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 2, 2000);
    }

    @Test
    public void schedule5TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 5, 2000);
    }

    @Test
    public void schedule10TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 10, 2000);
    }

    @Test
    public void schedule100TasksToBeRun() throws Exception {
        runTasks(newTaskProvider(), 100, 2000);
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

    private void runTasksWithNextTasks(final TaskProvider taskProvider, final int tasks, int sleepUntilUglyStop)
            throws InterruptedException, ScheduleFailedException {
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("thread-worker-");
        startingTest(tasks);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(1000, sleepingTask(1000)));
            final Thread t = threadFactory.newThread(newWorker(taskProvider));
            threads.add(t);
            t.start();
        }
        sleepingUntilUglyStop(sleepUntilUglyStop);
        interruptAndJoin(threads);
        assertForNextTasks(tasks);
        done();
    }

    private void assertForNextTasks(final int tasks) {
        assertEquals("All tasks were not run!", tasks * 2, this.tasksRun.intValue());
    }

    private static void interruptAndJoin(final List<Thread> threads) throws InterruptedException {
        log().debug("All work done, interrupting the threads");
        for (final Thread t : threads) {
            t.interrupt();
        }
        log().debug("Join.");
        for (final Thread t : threads) {
            t.join();
        }
    }

    private static void sleepingUntilUglyStop(final int sleep) throws InterruptedException {
        log().debug("sleep until ugly stop");
        Thread.sleep(sleep);
        log().debug("Temporary ugly stop");
    }

    private static void startingTest(final int tasks) {
        log().debug("Going to create and start {} worker threads for running the tasks with next tasks.", tasks);
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
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("thread-worker-");
        log().debug("Going to create and start {} worker threads for running the tasks.", tasks);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(1000));
            final Thread t = threadFactory.newThread(newWorker(taskProvider));
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

    private static void schedule(final TaskProvider taskProvider, final TestTask task) throws ScheduleFailedException {
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

    enum Category implements TestTaskCategory {
        One
    }

    private void runTasks(final TaskProvider taskProvider, final int tasks, int sleep)
            throws InterruptedException, ScheduleFailedException {
        final ThreadFactory threadFactory = NamedThreadFactory.forNamePrefix("thread-worker-");
        log().debug("Going to create and start {} worker threads for running the tasks.", tasks);
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            schedule(taskProvider, sleepingTask(1000));
            final Thread t = threadFactory.newThread(newWorker(taskProvider));
            threads.add(t);
            t.start();
        }
        log().debug("sleep until ugly stop");
        Thread.sleep(sleep);
        log().debug("Temporary ugly stop");
        interruptAndJoin(threads);
        assertEquals("All tasks were not run!", tasks, this.tasksRun.intValue());
        done();
    }

    private TestTask sleepingTask(final int sleep) {
        return sleepingTask(sleep, null, TimeSpecificationFactory.now());
    }

    private TestTask sleepingTask(final int sleep, final TestTask nextTask) {
        return sleepingTask(sleep, nextTask, TimeSpecificationFactory.now());
    }

    private TestTask sleepingTask(final int sleep, final TestTask nextTask, final Time when) {
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

    private static Logger log() {
        return LOG;
    }
}
