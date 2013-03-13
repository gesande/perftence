package net.sf.perftence.backlog;

import net.sf.simplebacklog.DefaultBacklogFactory;

import org.junit.Test;

public class PerftenceBacklogTest {

    @SuppressWarnings("static-method")
    @Test
    public void show() {
        new PerftenceBacklog(new DefaultBacklogFactory()).show();
    }
}
