package net.sf.völundr.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NamedThreadFactory uses a namePrefix (given in constructor) and index number
 * for making the thread name.
 */
public final class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger id = new AtomicInteger(0);
    private final String namePrefix;

    private NamedThreadFactory(final String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * A Thread with named prefix and index number will be returned here.
     */
    @Override
    public Thread newThread(final Runnable runnable) {
        return new Thread(runnable, prefix() + id().getAndIncrement());
    }

    private String prefix() {
        return this.namePrefix;
    }

    private AtomicInteger id() {
        return this.id;
    }

    /**
     * Factory method for creating NamedThreadFactory.
     */
    public static ThreadFactory forNamePrefix(final String namePrefix) {
        return new NamedThreadFactory(namePrefix);
    }

}