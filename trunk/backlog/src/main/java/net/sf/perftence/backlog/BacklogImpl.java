package net.sf.perftence.backlog;

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
    public TaskList<Backlog, DoneTask> done(String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, DoneTask>() {

            @Override
            public Backlog tasks(final DoneTask... tasks) {
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
    public TaskList<Backlog, WaitingTask> waiting(final String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, WaitingTask>() {

            @Override
            public Backlog tasks(final WaitingTask... tasks) {
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
    public TaskList<Backlog, InProgressTask> inProgress(final String title) {
        appender().subTitle(title);
        return new TaskList<Backlog, InProgressTask>() {

            @Override
            public Backlog tasks(final InProgressTask... tasks) {
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
