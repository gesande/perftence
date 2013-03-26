package net.sf.mybacklog;

public class DefaultBacklogAppender implements BacklogAppender {

    private final TaskAppender<Done> doneTaskAppender;
    private final TaskAppender<InProgress> inProgressAppender;
    private final TaskAppender<Waiting> waitingAppender;
    private final Appender appender;

    public DefaultBacklogAppender(final Appender appender,
            final TaskAppender<Done> doneAppender,
            final TaskAppender<InProgress> inProgressAppender,
            final TaskAppender<Waiting> waitingAppender) {
        this.appender = appender;
        this.doneTaskAppender = doneAppender;
        this.inProgressAppender = inProgressAppender;
        this.waitingAppender = waitingAppender;
    }

    @Override
    public void title(final String title) {
        appender().append(title).newLine().newLine();
    }

    @Override
    public void subTitle(final String title) {
        appender().tab().append(title).newLine().newLine();
    }

    @Override
    public void done(final Done... tasks) {
        done().append(tasks);
    }

    @Override
    public void waiting(final Waiting... tasks) {
        waiting().append(tasks);
    }

    @Override
    public void inProgress(final InProgress... tasks) {
        inProgress().append(tasks);
    }

    @Override
    public String build() {
        return appender().build();
    }

    private Appender appender() {
        return this.appender;
    }

    private TaskAppender<Waiting> waiting() {
        return this.waitingAppender;
    }

    private TaskAppender<Done> done() {
        return this.doneTaskAppender;
    }

    private TaskAppender<InProgress> inProgress() {
        return this.inProgressAppender;
    }

}
