package net.sf.perftence.backlog;

import net.sf.simplebacklog.BacklogFactoryUsingSysoutAndChalks;

public class PerftenceBacklogMain {
    public static void main(String[] args) {
        new PerftenceBacklog(new BacklogFactoryUsingSysoutAndChalks()).show();
    }
}
