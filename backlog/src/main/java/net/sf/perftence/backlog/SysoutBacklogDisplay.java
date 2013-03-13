package net.sf.perftence.backlog;

public class SysoutBacklogDisplay implements BacklogDisplay {

    @Override
    public void display(final BacklogAppender appender) {
        System.out.println(appender.build());
    }

}
