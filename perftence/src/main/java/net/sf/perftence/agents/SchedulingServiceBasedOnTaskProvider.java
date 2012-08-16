package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.perftence.TestFailureNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SchedulingServiceBasedOnTaskProvider implements
        TestTaskSchedulingService {
    private static final Logger LOG = LoggerFactory
            .getLogger(SchedulingServiceBasedOnTaskProvider.class);
    private final TaskProvider taskProvider;
    private final List<Thread> threads;
    private final List<Worker> workers;
    private final RunnableAdapter runnableProvider;
    private final TestFailureNotifier testFailureNotifier;
    private int workerAmount;

    public SchedulingServiceBasedOnTaskProvider(
            final TaskProvider taskProvider,
            final RunnableAdapter runnableProvider, final int workerAmount,
            final TestFailureNotifier testFailureNotifier) {
        this.runnableProvider = runnableProvider;
        this.testFailureNotifier = testFailureNotifier;
        workerAmount(workerAmount);
        this.taskProvider = taskProvider;
        this.threads = new ArrayList<Thread>();
        this.workers = new ArrayList<Worker>();
    }

    private void workerAmount(final int workerAmount) {
        this.workerAmount = workerAmount;
    }

    private TaskProvider taskProvider() {
        return this.taskProvider;
    }

    private RunnableAdapter runnableProvider() {
        return this.runnableProvider;
    }

    private boolean hasScheduledTasks() {
        return taskProvider().hasScheduledTasks();
    }

    @Override
    public void scheduleTask(final TestTask task)
            throws ScheduleFailedException {
        taskProvider().schedule(task);
    }

    @Override
    public void shutdown() {
        interruptWorkerThreads();
        waitWorkerThreadsToDie();
        clearLists();
    }

    private void waitWorkerThreadsToDie() {
        for (final Thread t : threads()) {
            try {
                t.join();
            } catch (InterruptedException e) {
                log().error("Thread " + t.getName() + " was interrupted!", e);
            }
        }
        log().debug("All worker threads died.");
    }

    private void interruptWorkerThreads() {
        for (final Thread t : threads()) {
            t.interrupt();
        }
        log().debug("All worker threads interrupted.");
    }

    @Override
    public void handleFailure(final TestTask task, final Throwable cause) {
        // no implementation provided
    }

    @Override
    public void markDone(final TestTask task) {
        // no implementation provided
    }

    @Override
    public void scheduleFirstTasks(final Collection<TestAgent> agents)
            throws ScheduleFailedException {
        if (agents.isEmpty()) {
            log().info("No agents defined, skipping the whole thingy...");
            return;
        }
        validateWorkerAmount(agents.size());
        clearLists();
        firstTaskScheduling(agents);
        createWorkerThreads();
    }

    private void createWorkerThreads() {
        log().debug("Start creating {} worker threads.", workerAmount());
        for (int i = 0; i < workerAmount(); i++) {
            addNewWorkerThread(i);
        }
        log().debug("{} worker threads ready to be run.", workerAmount());
    }

    private void clearLists() {
        threads().clear();
        workers().clear();
    }

    private void validateWorkerAmount(final int agents)
            throws ScheduleFailedException {
        if (workerAmount() == -1) {
            workerAmount(agents);
        }
        if (workerAmount() == 0) {
            throw new ScheduleFailedException("No workers were defined!");
        }
    }

    private void firstTaskScheduling(final Collection<TestAgent> agents)
            throws ScheduleFailedException {
        log().debug("Task scheduling start.");
        for (final TestAgent agent : agents) {
            scheduleTask(agent.firstTask());
        }
        log().debug("Task scheduling done.");
    }

    private void addNewWorkerThread(final int i) {
        threads().add(
                newThread(addWorker(new Worker(taskProvider(),
                        runnableProvider(), failureNotifier())),
                        workerThreadName(i)));
    }

    private TestFailureNotifier failureNotifier() {
        return this.testFailureNotifier;
    }

    private static Thread newThread(final Runnable runnable, final String name) {
        return new Thread(runnable, name);
    }

    private Worker addWorker(final Worker worker) {
        workers().add(worker);
        return worker;
    }

    private static String workerThreadName(final int i) {
        return "worker-thread-" + i;
    }

    @Override
    public void run() {
        log().debug("Starting worker threads.");
        for (final Thread t : threads()) {
            t.start();
        }
        log().debug("Worker threads started.");
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log().error(
                        "Thread " + Thread.currentThread().getName()
                                + " was interrupted!", e);
            }
        } while (hasScheduledTasks() || workersAreBusy());
        log().debug("All worker threads finished.");
    }

    private boolean workersAreBusy() {
        for (final Worker worker : workers()) {
            if (worker.isBusy()) {
                return true;
            }
        }
        return false;
    }

    private List<Thread> threads() {
        return this.threads;
    }

    private synchronized List<Worker> workers() {
        return this.workers;
    }

    private static Logger log() {
        return LOG;
    }

    private int workerAmount() {
        return this.workerAmount;
    }

}
