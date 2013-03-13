package net.sf.simplebacklog;

public class BacklogImpl implements Backlog {
    private final BacklogAppender appender;
    private final BacklogDisplay display;

    public BacklogImpl(final BacklogAppender appender,
            final BacklogDisplay display) {
        this.appender = appender;
        this.display = display;
    }

    @Override
    public Backlog title(String title) {
        appender().title(title);
        return this;
    }

    private BacklogAppender appender() {
        return this.appender;
    }

    @Override
    public TaskList<Backlog, Done> done(String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, Done>() {

            @Override
            public Backlog tasks(final Done... tasks) {
                appender().done(tasks);
                return BacklogImpl.this;
            }

            @Override
            public Backlog noTasks() {
                return BacklogImpl.this;
            }
        };
    }

    @Override
    public TaskList<Backlog, Waiting> waiting(final String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, Waiting>() {

            @Override
            public Backlog tasks(final Waiting... tasks) {
                appender().waiting(tasks);
                return BacklogImpl.this;
            }

            @Override
            public Backlog noTasks() {
                return BacklogImpl.this;
            }
        };
    }

    @Override
    public TaskList<Backlog, InProgress> inProgress(final String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, InProgress>() {

            @Override
            public Backlog tasks(final InProgress... tasks) {
                appender().inProgress(tasks);
                return BacklogImpl.this;
            }

            @Override
            public Backlog noTasks() {
                return BacklogImpl.this;
            }
        };
    }

    @Override
    public void show() {
        display().display(appender());
    }

    private BacklogDisplay display() {
        return this.display;
    }

}
