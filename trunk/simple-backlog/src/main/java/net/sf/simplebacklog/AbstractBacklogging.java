package net.sf.simplebacklog;

public abstract class AbstractBacklogging {
    private TaskFactory taskFactory;

    public AbstractBacklogging() {
        this.taskFactory = new TaskFactory();
    }

    protected final InProgress inProgress(final String title, final Tag tag) {
        return taskFactory().inProgress(title, tag);
    }

    protected final Waiting waiting(final String title, final Tag tag) {
        return taskFactory().waiting(title, tag);
    }

    protected final Done done(final String title, final Tag tag) {
        return taskFactory().done(title, tag);
    }

    protected abstract Backlog newBacklog();

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }

}
