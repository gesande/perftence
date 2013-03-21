package net.sf.simplebacklog;

public final class BacklogImpl implements Backlog {
    private final BacklogAppender appender;
    private final BacklogDisplay display;
    private final TaskListFactory taskListFactory;

    public BacklogImpl(final BacklogAppender appender,
            final BacklogDisplay display, final TaskListFactory taskListFactory) {
        this.appender = appender;
        this.display = display;
        this.taskListFactory = taskListFactory;
    }

    @Override
    public Backlog title(String title) {
        appender().title(title);
        return this;
    }

    @Override
    public TaskList<Backlog, Done> done() {
        return taskListFactory().forDone(this);
    }

    @Override
    public TaskList<Backlog, Waiting> waiting() {
        return taskListFactory().forWaiting(this);
    }

    @Override
    public TaskList<Backlog, InProgress> inProgress() {
        return taskListFactory().forInProgress(this);
    }

    @Override
    public void show() {
        display().display(appender());
    }

    private BacklogDisplay display() {
        return this.display;
    }

    private BacklogAppender appender() {
        return this.appender;
    }

    private TaskListFactory taskListFactory() {
        return this.taskListFactory;
    }
}
