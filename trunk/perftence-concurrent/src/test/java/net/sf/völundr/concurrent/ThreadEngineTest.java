package net.sf.völundr.concurrent;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class ThreadEngineTest {

    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public void nullRunnables() {
        newEngineWithNamedThreadFactory().run((Runnable[]) null);
    }

    @SuppressWarnings("static-method")
    @Test
    public void empty() {
        newEngineWithNamedThreadFactory().run(new Runnable[0]);
    }

    @SuppressWarnings("static-method")
    @Test
    public void runRunnables() {
        final AtomicBoolean runned = new AtomicBoolean(false);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                runned.set(true);
            }
        };
        newEngineWithNamedThreadFactory().run(new Runnable[] { r });
        assertTrue(runned.get());
    }

    @SuppressWarnings("static-method")
    @Test
    public void interruptThreads() throws InterruptedException {
        final AtomicBoolean wasInterrupted = new AtomicBoolean(false);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    wasInterrupted.set(true);
                }
            }
        };
        final ThreadEngine newEngineWithNamedThreadFactory = newEngineWithNamedThreadFactory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                newEngineWithNamedThreadFactory.run(new Runnable[] { r });
            }
        }).start();
        Thread.sleep(100);
        new Thread(new Runnable() {

            @Override
            public void run() {
                newEngineWithNamedThreadFactory.interruptThreads();
            }
        }).start();
        Thread.sleep(100);
        assertTrue(wasInterrupted.get());
    }

    private static ThreadEngine newEngineWithNamedThreadFactory() {
        return new ThreadEngine(
                NamedThreadFactory.forNamePrefix("my-test-threads-"));
    }

}
