package net.sf.simplebacklog;

public class DefaultBacklogAppender implements BacklogAppender {

    private final StringBuilder sb = new StringBuilder();
    private final DoneAppender doneTaskAppender;
    private final InProgressAppender inProgressAppender;
    private final WaitingAppender waitingAppender;

    public DefaultBacklogAppender() {
        this.doneTaskAppender = new DoneAppender(appender());
        this.inProgressAppender = new InProgressAppender(appender());
        this.waitingAppender = new WaitingAppender(appender());
    }

    private static class DoneAppender implements TaskAppender<Done> {

        private StringBuilder parent;

        public DoneAppender(final StringBuilder parent) {
            this.parent = parent;
        }

        @Override
        public void append(final Done... tasks) {
            for (final Done task : tasks) {
                parent().append(tab()).append("+++ ").append(task.title())
                        .append(" +++ ").append("#").append(task.tag().name())
                        .append(endOfLine());
            }
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class InProgressAppender implements TaskAppender<InProgress> {

        private StringBuilder parent;

        public InProgressAppender(final StringBuilder parent) {
            this.parent = parent;
        }

        @Override
        public void append(final InProgress... tasks) {
            for (final InProgress task : tasks) {
                parent().append(tab()).append("    ").append(task.title())
                        .append("     ").append("#").append(task.tag().name())
                        .append(endOfLine());
            }
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class WaitingAppender implements TaskAppender<Waiting> {

        private StringBuilder parent;

        public WaitingAppender(final StringBuilder parent) {
            this.parent = parent;
        }

        @Override
        public void append(final Waiting... tasks) {
            for (final Waiting task : tasks) {
                parent().append(tab()).append("--- ").append(task.title())
                        .append(" --- ").append("#").append(task.tag().name())
                        .append(endOfLine());
            }
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    @Override
    public void title(String title) {
        appender().append(title).append(endOfLine()).append(endOfLine());
    }

    @Override
    public void subTitle(String title) {
        appender().append(tab()).append(title).append(endOfLine())
                .append(endOfLine());
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

    private static String endOfLine() {
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
