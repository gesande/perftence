package net.sf.perftence.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger id = new AtomicInteger(0);
    private final String namePrefix;

    private NamedThreadFactory(final String namePrefix) {
        this.namePrefix = namePrefix;
    }

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

    public static ThreadFactory forNamePrefix(final String namePrefix) {
        return new NamedThreadFactory(namePrefix);
    }

}