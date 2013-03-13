package net.sf.simplebacklog;

public class DefaultBacklogFactory implements BacklogFactory {

    @Override
    public Backlog newBacklog() {
        return new BacklogImpl(new DefaultBacklogAppender(),
                new SysoutBacklogDisplay());
    }

}
