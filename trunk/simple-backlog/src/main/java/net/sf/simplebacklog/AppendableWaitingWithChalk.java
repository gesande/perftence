package net.sf.simplebacklog;

public class AppendableWaitingWithChalk implements Appendable<Waiting> {
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