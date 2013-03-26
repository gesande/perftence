package net.sf.perftence.backlog;

import net.sf.mybacklog.BacklogFactoryUsingChalks;
import net.sf.mybacklog.SysoutBacklogDisplay;

public class PerftenceBacklogMain {
    public static void main(String[] args) {
        new PerftenceBacklog(
                BacklogFactoryUsingChalks
                        .displayedBy(new SysoutBacklogDisplay())).show();
    }
}
