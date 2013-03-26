package net.sf.mybacklog;

public class SysoutBacklogDisplay implements BacklogDisplay {

    @Override
    public void display(final BacklogAppender appender) {
        System.out.println(appender.build());
    }

}
