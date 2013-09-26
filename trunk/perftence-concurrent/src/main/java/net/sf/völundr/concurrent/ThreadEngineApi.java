package net.sf.völundr.concurrent;

/**
 * Easy API for running something being or extending Runnable in threads.
 * 
 */
public final class ThreadEngineApi<RUNNABLE extends Runnable> {

    private RUNNABLE[] runnables;
    private ThreadEngine threadEngine;

    /**
     * When a thread is created this prefix is used as a part for its name.
     */
    public ThreadEngineApi<RUNNABLE> threadNamePrefix(final String prefix) {
        this.threadEngine = new ThreadEngine(
                NamedThreadFactory.forNamePrefix(prefix));
        return this;
    }

    public ThreadEngineApi<RUNNABLE> runnables(final RUNNABLE... runnables) {
        this.runnables = runnables;
        return this;
    }

    /**
     * This call blocks until all threads are finished.
     */
    public void run() {
        engine().run(runnables());
    }

    /**
     * Interrupts threads running the runnables.
     */
    public void interrupt() {
        engine().interruptThreads();
    }

    private RUNNABLE[] runnables() {
        return this.runnables;
    }

    private ThreadEngine engine() {
        return this.threadEngine;
    }

}