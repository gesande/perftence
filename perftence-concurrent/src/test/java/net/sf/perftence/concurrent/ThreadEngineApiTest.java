package net.sf.perftence.concurrent;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class ThreadEngineApiTest {

    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public void nullRunnables() {
        new ThreadEngineApi<Runnable>().with("my-test-threads-").run();
    }

    @SuppressWarnings("static-method")
    @Test
    public void empty() {
        new ThreadEngineApi<Runnable>().with("my-test-threads-")
                .with(new Runnable[0]).run();
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
        new ThreadEngineApi<Runnable>().with("my-test-threads-")
                .with(new Runnable[] { r }).run();
        assertTrue(runned.get());
    }
}
