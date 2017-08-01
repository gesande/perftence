package net.sf.perftence.agents;

import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.PerftenceRuntimeException;

final class SchedulingServiceBasedOnJavaConcurrentStuff implements TestTaskSchedulingService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingServiceBasedOnJavaConcurrentStuff.class);

    private final RunnableAdapter runnableProvider;
    private final ScheduledTasks scheduledTasks;
    private final ScheduledThreadPoolExecutor executor;

    public SchedulingServiceBasedOnJavaConcurrentStuff(final RunnableAdapter runnableProvider,
            final ScheduledTasks scheduledTasks, final ScheduledThreadPoolExecutor executor) {
        this.runnableProvider = runnableProvider;
        this.scheduledTasks = scheduledTasks;
        this.executor = executor;
    }

    private ScheduledThreadPoolExecutor executor() {
        return this.executor;
    }

    private void addToScheduledTasks(final TestTask task) {
        this.scheduledTasks.add(task);
    }

    @Override
    public void handleFailure(final TestTask task, final Throwable cause) {
        this.scheduledTasks.remove(task);
    }

    @Override
    public void markDone(final TestTask task) {
        if (task.nextTaskIfAny() != null) {
            scheduleTask(task.nextTaskIfAny());
        }
        this.scheduledTasks.remove(task);
    }

    @Override
    public synchronized void shutdown() {
        executor().shutdown();
        log().debug("Executor was shut down.");
    }

    @Override
    public void scheduleFirstTasks(final Collection<TestAgent> agents) {
        log().info("Start scheduling the first tasks...");
        for (final TestAgent agent : agents) {
            scheduleTask(agent.firstTask());
        }
        log().info("First tasks scheduled...ready to rock'n roll...");
    }

    private static Logger log() {
        return LOG;
    }

    private boolean hasScheduledTasks() {
        return this.scheduledTasks.hasScheduledTasks();
    }

    @Override
    public void scheduleTask(final TestTask task) {
        final Time when = task.when();
        addToScheduledTasks(task);
        schedule(runnableProvider().adapt(task, scheduledToBeRun(when, System.nanoTime())), when.time(),
                when.timeUnit());
    }

    private static long scheduledToBeRun(final Time when, final long now) {
        return TimeUnit.NANOSECONDS.convert(when.time(), when.timeUnit()) + now;
    }

    private RunnableAdapter runnableProvider() {
        return this.runnableProvider;
    }

    private void schedule(final Runnable command, final long delay, final TimeUnit unit) {
        executor().schedule(command, delay, unit);
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new PerftenceRuntimeException(e);
            }
        } while (hasScheduledTasks());
    }

}