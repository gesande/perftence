package net.sf.perftence.concurrent;

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

    private static ThreadEngine newEngineWithNamedThreadFactory() {
        return new ThreadEngine(
                NamedThreadFactory.forNamePrefix("my-test-threads-"));
    }

}
