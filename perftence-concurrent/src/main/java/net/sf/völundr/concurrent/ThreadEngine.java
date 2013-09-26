package net.sf.völundr.concurrent;

import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ThreadEngine {
    private final static Logger LOG = LoggerFactory
            .getLogger(ThreadEngine.class);
    private final ThreadFactory threadFactory;

    private Thread threads[] = null;

    public ThreadEngine(final ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    private Thread[] threads() {
        return this.threads;
    }

    /**
     * Running the given runnables, this call is blocking until all threads are
     * done.
     */
    public <RUNNABLE extends Runnable> void run(final RUNNABLE... runnables) {
        validate(runnables);
        if (runnables.length == 0) {
            log().info(
                    "There was nothing to do, no runnables were given. Exiting.");
            return;
        }
        initializeWith(runnables);
        startThreads();
        joinThreads();
        clearThreads();
    }

    private static <T extends Runnable> void validate(final T... runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("runnables is null");
        }
    }

    private void clearThreads() {
        this.threads = null;
        log().debug("Threads 'cleared'.");
    }

    private static Logger log() {
        return LOG;
    }

    private <T extends Runnable> void initializeWith(final T... runnables) {
        this.threads = new Thread[runnables.length];
        for (int i = 0; i < threads().length; i++) {
            this.threads[i] = threadFactory().newThread(runnables[i]);
        }
    }

    private ThreadFactory threadFactory() {
        return this.threadFactory;
    }

    private void startThreads() {
        log().debug("Starting  threads...");
        for (int i = 0; i < threads().length; i++) {
            threads()[i].start();
        }
        log().debug("Threads started.");
    }

    private void joinThreads() {
        try {
            for (int i = 0; i < threads().length; i++) {
                threads()[i].join();
            }
        } catch (final InterruptedException ignore) {
            log().warn("Thread join interrupted");
        }
    }

    public void interruptThreads() {
        log().debug("Interrupting threads...");
        for (int i = 0; i < threads().length; i++) {
            threads()[i].interrupt();
        }
        log().debug("Threads interrupted.");
    }

}