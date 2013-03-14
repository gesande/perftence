package net.sf.simplebacklog;

public class DefaultBacklogAppender implements BacklogAppender {

    private final StringBuilder sb = new StringBuilder();
    private final DoneAppender doneTaskAppender;
    private final InProgressAppender inProgressAppender;
    private final WaitingAppender waitingAppender;
    private final ChalkBox chalkBox = new ChalkBox();

    public DefaultBacklogAppender() {
        this.doneTaskAppender = new DoneAppender(appender(),
                new AppendableDoneWithChalk(chalkBox().green()));
        this.inProgressAppender = new InProgressAppender(appender(),
                new AppendableInProgressWithChalk(chalkBox().yellow()));
        this.waitingAppender = new WaitingAppender(appender(),
                new AppendableWaitingWithChalk(chalkBox().red()));
    }

    private ChalkBox chalkBox() {
        return this.chalkBox;
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
            final StringBuilder line = new StringBuilder();
            line.append(tab()).append("--- ").append(task.title())
                    .append(" --- ").append("#").append(task.tag().name());
            return chalk().write(line.toString());
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
            final StringBuilder line = new StringBuilder();
            line.append(tab()).append("    ").append(task.title())
                    .append("     ").append("#").append(task.tag().name())
                    .append(newLine());
            return chalk().write(line.toString());
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
            final StringBuilder taskLine = new StringBuilder();
            taskLine.append(tab()).append("+++ ").append(task.title())
                    .append(" +++ ").append("#").append(task.tag().name());
            return chalk().write(taskLine.toString());
        }

        private Chalk chalk() {
            return this.chalk;
        }
    }

    private static class DoneAppender implements TaskAppender<Done> {

        private final StringBuilder parent;
        private final Appendable<Done> doneAppendable;

        public DoneAppender(final StringBuilder parent,
                final Appendable<Done> doneAppendable) {
            this.parent = parent;
            this.doneAppendable = doneAppendable;
        }

        @Override
        public void append(final Done... tasks) {
            for (final Done task : tasks) {
                parent().append(appendable().build(task)).append(newLine());
            }
            parent().append(newLine());
        }

        private Appendable<Done> appendable() {
            return this.doneAppendable;
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class InProgressAppender implements TaskAppender<InProgress> {

        private final StringBuilder parent;
        private final Appendable<InProgress> inProgressAppendable;

        public InProgressAppender(final StringBuilder parent,
                final Appendable<InProgress> inProgressAppendable) {
            this.parent = parent;
            this.inProgressAppendable = inProgressAppendable;
        }

        @Override
        public void append(final InProgress... tasks) {
            for (final InProgress task : tasks) {
                parent().append(appendable().build(task)).append(newLine());
            }
            parent().append(newLine());
        }

        private Appendable<InProgress> appendable() {
            return this.inProgressAppendable;
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class WaitingAppender implements TaskAppender<Waiting> {

        private final StringBuilder parent;
        private final Appendable<Waiting> appendableWaiting;

        public WaitingAppender(final StringBuilder parent,
                Appendable<Waiting> appendableWaiting) {
            this.parent = parent;
            this.appendableWaiting = appendableWaiting;
        }

        @Override
        public void append(final Waiting... tasks) {
            for (final Waiting task : tasks) {
                parent().append(appendable().build(task)).append(newLine());
            }
            parent().append(newLine());
        }

        private Appendable<Waiting> appendable() {
            return this.appendableWaiting;
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    @Override
    public void title(String title) {
        appender().append(title).append(newLine()).append(newLine());
    }

    @Override
    public void subTitle(String title) {
        appender().append(tab()).append(title).append(newLine())
                .append(newLine());
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
        return appender().toString();
    }

    private StringBuilder appender() {
        return this.sb;
    }

    private static String newLine() {
        return "\n";
    }

    private static String tab() {
        return "  ";
    }

    private WaitingAppender waiting() {
        return this.waitingAppender;
    }

    private DoneAppender done() {
        return this.doneTaskAppender;
    }

    private InProgressAppender inProgress() {
        return this.inProgressAppender;
    }

}
