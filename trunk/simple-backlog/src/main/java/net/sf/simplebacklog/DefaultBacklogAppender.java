package net.sf.simplebacklog;


public class DefaultBacklogAppender implements BacklogAppender {

    private final StringBuilder sb = new StringBuilder();
    private final DoneAppender doneTaskAppender;
    private final InProgressAppender inProgressAppender;
    private final WaitingAppender waitingAppender;
    private final ChalkBox chalkBox = new ChalkBox();

    public DefaultBacklogAppender() {
        this.doneTaskAppender = new DoneAppender(appender(), chalkBox().green());
        this.inProgressAppender = new InProgressAppender(appender(), chalkBox()
                .yellow());
        this.waitingAppender = new WaitingAppender(appender(), chalkBox().red());
    }

    private ChalkBox chalkBox() {
        return this.chalkBox;
    }

    private static class DoneAppender implements TaskAppender<Done> {

        private final StringBuilder parent;
        private final Chalk chalk;

        public DoneAppender(final StringBuilder parent, Chalk chalk) {
            this.parent = parent;
            this.chalk = chalk;
        }

        private Chalk chalk() {
            return this.chalk;
        }

        @Override
        public void append(final Done... tasks) {
            for (final Done task : tasks) {
                final StringBuilder taskLine = new StringBuilder();
                taskLine.append(tab()).append("+++ ").append(task.title())
                        .append(" +++ ").append("#").append(task.tag().name());
                parent().append(chalk().write(taskLine.toString())).append(
                        endOfLine());
            }
            parent().append(endOfLine());
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class InProgressAppender implements TaskAppender<InProgress> {

        private final StringBuilder parent;
        private final Chalk chalk;

        public InProgressAppender(final StringBuilder parent, final Chalk chalk) {
            this.parent = parent;
            this.chalk = chalk;
        }

        @Override
        public void append(final InProgress... tasks) {
            for (final InProgress task : tasks) {
                final StringBuilder line = new StringBuilder();
                line.append(tab()).append("    ").append(task.title())
                        .append("     ").append("#").append(task.tag().name())
                        .append(endOfLine());
                parent().append(chalk().write(line.toString()));
            }
        }

        private Chalk chalk() {
            return this.chalk;
        }

        private StringBuilder parent() {
            return this.parent;
        }
    }

    private static class WaitingAppender implements TaskAppender<Waiting> {

        private final StringBuilder parent;
        private final Chalk chalk;

        public WaitingAppender(final StringBuilder parent, Chalk chalk) {
            this.parent = parent;
            this.chalk = chalk;
        }

        @Override
        public void append(final Waiting... tasks) {
            for (final Waiting task : tasks) {
                final StringBuilder line = new StringBuilder();
                line.append(tab()).append("--- ").append(task.title())
                        .append(" --- ").append("#").append(task.tag().name())
                        .append(endOfLine());
                parent().append(chalk().write(line.toString()));
            }
        }

        private Chalk chalk() {
            return this.chalk;
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
