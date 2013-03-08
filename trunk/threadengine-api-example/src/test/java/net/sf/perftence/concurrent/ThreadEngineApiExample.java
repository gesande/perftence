package net.sf.perftence.concurrent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadEngineApiExample {

    private final static Logger LOG = LoggerFactory
            .getLogger(ThreadEngineApiExample.class);

    @SuppressWarnings("static-method")
    @Test
    public void example() {
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                log().info("My runnable got run. Nice :)");
            }

        };
        new ThreadEngineApi<Runnable>().with("threadname-prefix")
                .with(runnable).run();
    }

    private static Logger log() {
        return LOG;
    }
}
