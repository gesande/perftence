package net.sf.mybacklog;

public class DefaultTaskListFactory implements TaskListFactory {
    private final BacklogAppender appender;

    public DefaultTaskListFactory(final BacklogAppender appender) {
        this.appender = appender;
    }

    @Override
    public TaskList<Backlog, Done> forDone(final Backlog backlog) {
        return new TaskList<Backlog, Done>() {

            @Override
            public Backlog tasks(final Done... tasks) {
                backlogAppender().done(tasks);
                return backlog;
            }

            @Override
            public Backlog noTasks() {
                return backlog;
            }

            @Override
            public TaskList<Backlog, Done> title(final String title) {
                backlogAppender().subTitle(title);
                return this;
            }
        };
    }

    @Override
    public TaskList<Backlog, Waiting> forWaiting(final Backlog backlog) {
        return new TaskList<Backlog, Waiting>() {

            @Override
            public Backlog tasks(final Waiting... tasks) {
                backlogAppender().waiting(tasks);
                return backlog;
            }

            @Override
            public Backlog noTasks() {
                return backlog;
            }

            @Override
            public TaskList<Backlog, Waiting> title(final String title) {
                backlogAppender().subTitle(title);
                return this;
            }
        };
    }

    @Override
    public TaskList<Backlog, InProgress> forInProgress(final Backlog backlog) {
        return new TaskList<Backlog, InProgress>() {

            @Override
            public Backlog tasks(final InProgress... tasks) {
                backlogAppender().inProgress(tasks);
                return backlog;
            }

            @Override
            public Backlog noTasks() {
                return backlog;
            }

            @Override
            public TaskList<Backlog, InProgress> title(final String title) {
                backlogAppender().subTitle(title);
                return this;
            }
        };
    }

    private BacklogAppender backlogAppender() {
        return this.appender;
    }
}
