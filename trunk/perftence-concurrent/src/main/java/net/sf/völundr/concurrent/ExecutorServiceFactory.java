package net.sf.völundr.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ExecutorServiceFactory {

    @SuppressWarnings("static-method")
    public ExecutorService newFixedThreadPool(final int threads,
            final String threadNamePrefix) {
        return Executors.newFixedThreadPool(threads,
                NamedThreadFactory.forNamePrefix(threadNamePrefix));
    }

}
