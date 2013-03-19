package net.sf.simplebacklog;

public class BacklogFactoryUsingSysoutAndChalks implements BacklogFactory {

    @Override
    public Backlog newBacklog() {
        final ChalkBox chalkBox = new ChalkBox();
        final StringBuilderAppender appender = new StringBuilderAppender();
        final Chalk green = chalkBox.green();
        final DoneAppender doneAppender = new DoneAppender(appender,
                new AppendableDoneWithChalk(green));
        final Chalk yellow = chalkBox.yellow();
        final TaskAppender<InProgress> inProgressAppender = new InProgressAppender(
                appender, new AppendableInProgressWithChalk(yellow));
        final Chalk red = chalkBox.red();
        final TaskAppender<Waiting> waitingAppender = new WaitingAppender(
                appender, new AppendableWaitingWithChalk(red));
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
        return new BacklogImpl(backlogAppender, new SysoutBacklogDisplay(),
                taskListFactory);
    }

    private interface Appendable<TASK extends Task> {
        String build(TASK task);
    }

    private static class AppendableWaitingWithChalk implements
            Appendable<Waiting> {
        private final Chalk chalk;

        public AppendableWaitingWithChalk(final Chalk chalk) {
            this.chalk = chalk;

        }

        @Override
        public String build(final Waiting task) {
            final StringBuilderAppender line = new StringBuilderAppender();
            line.tab().append("--- ").append(task.title()).append(" --- ")
                    .append("#").append(task.tag().name());
            return chalk().write(line.build());
        }

        private Chalk chalk() {
            return this.chalk;
        }
    }

    private static class AppendableInProgressWithChalk implements
            Appendable<InProgress> {
        private final Chalk chalk;

        public AppendableInProgressWithChalk(final Chalk chalk) {
            this.chalk = chalk;
        }

        @Override
        public String build(final InProgress task) {
            final StringBuilderAppender line = new StringBuilderAppender();
            line.tab().append("    ").append(task.title()).append("     ")
                    .append("#").append(task.tag().name());
            return chalk().write(line.build());
        }

        private Chalk chalk() {
            return this.chalk;
        }
    }

    private static class AppendableDoneWithChalk implements Appendable<Done> {
        private final Chalk chalk;

        public AppendableDoneWithChalk(final Chalk chalk) {
            this.chalk = chalk;
        }

        @Override
        public String build(final Done task) {
            final StringBuilderAppender line = new StringBuilderAppender();
            line.tab().append("+++ ").append(task.title()).append(" +++ ")
                    .append("#").append(task.tag().name());
            return chalk().write(line.build());
        }

        private Chalk chalk() {
            return this.chalk;
        }
    }

    private static class DoneAppender implements TaskAppender<Done> {

        private final Appender parent;
        private final Appendable<Done> doneAppendable;

        public DoneAppender(final Appender parent,
                final Appendable<Done> doneAppendable) {
            this.parent = parent;
            this.doneAppendable = doneAppendable;
        }

        @Override
        public void append(final Done... tasks) {
            for (final Done task : tasks) {
                parent().append(appendable().build(task)).newLine();
            }
            parent().newLine();
        }

        private Appendable<Done> appendable() {
            return this.doneAppendable;
        }

        private Appender parent() {
            return this.parent;
        }
    }

    private static class InProgressAppender implements TaskAppender<InProgress> {

        private final Appender parent;
        private final Appendable<InProgress> inProgressAppendable;

        public InProgressAppender(final Appender parent,
                final Appendable<InProgress> inProgressAppendable) {
            this.parent = parent;
            this.inProgressAppendable = inProgressAppendable;
        }

        @Override
        public void append(final InProgress... tasks) {
            for (final InProgress task : tasks) {
                parent().append(appendable().build(task)).newLine();
            }
            parent().newLine();
        }

        private Appendable<InProgress> appendable() {
            return this.inProgressAppendable;
        }

        private Appender parent() {
            return this.parent;
        }
    }

    private static class WaitingAppender implements TaskAppender<Waiting> {

        private final Appender parent;
        private final Appendable<Waiting> appendableWaiting;

        public WaitingAppender(final Appender parent,
                Appendable<Waiting> appendableWaiting) {
            this.parent = parent;
            this.appendableWaiting = appendableWaiting;
        }

        @Override
        public void append(final Waiting... tasks) {
            for (final Waiting task : tasks) {
                parent().append(appendable().build(task)).newLine();
            }
            parent().newLine();
        }

        private Appendable<Waiting> appendable() {
            return this.appendableWaiting;
        }

        private Appender parent() {
            return this.parent;
        }
    }

}
