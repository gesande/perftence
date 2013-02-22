package net.sf.perftence.concurrent;

public final class ThreadEngineApi<RUNNABLE extends Runnable> {

    private RUNNABLE[] runnables;
    private ThreadEngine threadEngine;

    public ThreadEngineApi<RUNNABLE> with(final String namePrefix) {
        this.threadEngine = new ThreadEngine(
                NamedThreadFactory.forNamePrefix(namePrefix));
        return this;
    }

    public ThreadEngineApi<RUNNABLE> with(final RUNNABLE... runnables) {
        this.runnables = runnables;
        return this;
    }

    public void run() {
        engine().run(runnables());
    }

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