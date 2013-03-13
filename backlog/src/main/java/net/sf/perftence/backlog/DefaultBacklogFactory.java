package net.sf.perftence.backlog;

public class DefaultBacklogFactory implements BacklogFactory {

    @Override
    public Backlog newBacklog() {
        return new BacklogImpl(new DefaultBacklogAppender(),
                new SysoutBacklogDisplay());
    }

}
