package net.sf.perftence.backlog;

import net.sf.simplebacklog.BacklogFactoryUsingChalks;
import net.sf.simplebacklog.SysoutBacklogDisplay;

public class PerftenceBacklogMain {
    public static void main(String[] args) {
        new PerftenceBacklog(
                BacklogFactoryUsingChalks
                        .displayedBy(new SysoutBacklogDisplay())).show();
    }
}
