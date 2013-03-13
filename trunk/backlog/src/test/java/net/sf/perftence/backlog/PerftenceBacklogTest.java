package net.sf.perftence.backlog;

import org.junit.Test;

public class PerftenceBacklogTest {

    @SuppressWarnings("static-method")
    @Test
    public void show() {
        new PerftenceBacklog(new DefaultBacklogFactory()).show();
    }
}
