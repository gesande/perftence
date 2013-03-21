package net.sf.simplebacklog;

public class BacklogFactoryUsingChalks implements BacklogFactory {

    private final BacklogDisplay display;

    private BacklogFactoryUsingChalks(final BacklogDisplay display) {
        this.display = display;
    }

    public static BacklogFactory displayedBy(final BacklogDisplay display) {
        return new BacklogFactoryUsingChalks(display);
    }

    @Override
    public Backlog newBacklog() {
        final ChalkBox chalkBox = new ChalkBox();
        final StringBuilderAppender appender = new StringBuilderAppender();

        final Chalk green = chalkBox.green();
        final TaskAppender<Done> doneAppender = new DoneAppender(appender,
                new ChalkedDone(green, new DoneAsAppender()));

        final Chalk yellow = chalkBox.yellow();
        final TaskAppender<InProgress> inProgressAppender = new InProgressAppender(
                appender, new ChalkedInProgress(yellow,
                        new InProgressAsAppender()));

        final Chalk red = chalkBox.red();
        final TaskAppender<Waiting> waitingAppender = new WaitingAppender(
                appender, new ChalkedWaiting(red, new WaitingAsAppender()));


        final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                appender, doneAppender, inProgressAppender, waitingAppender);

        
        final TaskListFactory taskListFactory = new TaskListFactory() {
            @Override
            public TaskList<Backlog, Done> forDone(final Backlog backlog) {
                return new TaskList<Backlog, Done>() {

                    @Override
                    public Backlog tasks(final Done... tasks) {
                        backlogAppender.done(tasks);
                        return backlog;
                    }

                    @Override
                    public Backlog noTasks() {
                        return backlog;
                    }

                    @Override
                    public TaskList<Backlog, Done> title(final String title) {
                        backlogAppender.subTitle(green.write(title));
                        return this;
                    }
                };
            }

            @Override
            public TaskList<Backlog, Waiting> forWaiting(final Backlog backlog) {
                return new TaskList<Backlog, Waiting>() {

                    @Override
                    public Backlog tasks(final Waiting... tasks) {
                        backlogAppender.waiting(tasks);
                        return backlog;
                    }

                    @Override
                    public Backlog noTasks() {
                        return backlog;
                    }

                    @Override
                    public TaskList<Backlog, Waiting> title(final String title) {
                        backlogAppender.subTitle(red.write(title));
                        return this;
                    }
                };
            }

            @Override
            public TaskList<Backlog, InProgress> forInProgress(
                    final Backlog backlog) {
                return new TaskList<Backlog, InProgress>() {

                    @Override
                    public Backlog tasks(final InProgress... tasks) {
                        backlogAppender.inProgress(tasks);
                        return backlog;
                    }

                    @Override
                    public Backlog noTasks() {
                        return backlog;
                    }

                    @Override
                    public TaskList<Backlog, InProgress> title(
                            final String title) {
                        backlogAppender.subTitle(yellow.write(title));
                        return this;
                    }
                };
            }
        };
        return new BacklogImpl(backlogAppender, display(), taskListFactory);
    }

    private BacklogDisplay display() {
        return this.display;
    }

}
