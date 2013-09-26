package net.sf.völundr.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NamedThreadFactoryTest {

    @SuppressWarnings("static-method")
    @Test
    public void newThread() {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                //
            }
        };
        final Thread newThread = NamedThreadFactory.forNamePrefix("prefix")
                .newThread(r);
        assertNotNull(newThread);
        final String name = newThread.getName();
        assertTrue(name.contains("prefix"));
        assertTrue(name.startsWith("prefix"));
    }
}
