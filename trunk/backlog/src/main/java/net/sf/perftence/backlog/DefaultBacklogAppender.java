package net.sf.perftence.backlog;

public class DefaultBacklogAppender implements BacklogAppender {
    private final StringBuilder sb = new StringBuilder();

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
    public void done(final DoneTask... tasks) {
        for (final DoneTask task : tasks) {
            appender().append(tab()).append("+++ ").append(task.title())
                    .append(" +++ ").append("#").append(task.tag().name())
                    .append(endOfLine());
        }
    }

    @Override
    public void waiting(final WaitingTask... tasks) {
        for (final WaitingTask task : tasks) {
            appender().append(tab()).append("--- ").append(task.title())
                    .append(" --- ").append("#").append(task.tag().name())
                    .append(endOfLine());
        }
    }

    @Override
    public void inProgress(final InProgressTask... tasks) {
        for (final InProgressTask task : tasks) {
            appender().append(tab()).append("    ").append(task.title())
                    .append("     ").append("#").append(task.tag().name())
                    .append(endOfLine());
        }
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

}
