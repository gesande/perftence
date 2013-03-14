package net.sf.perftence.backlog;

import net.sf.simplebacklog.DefaultBacklogFactory;

public class PerftenceBacklogMain {
    public static void main(String[] args) {
        new PerftenceBacklog(new DefaultBacklogFactory()).show();
    }
}
