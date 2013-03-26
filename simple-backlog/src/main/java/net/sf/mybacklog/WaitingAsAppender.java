package net.sf.mybacklog;

final class WaitingAsAppender implements AppenderAs<Waiting> {

    @Override
    public Appender task(final Waiting task) {
        final StringBuilderAppender line = new StringBuilderAppender();
        line.tab().append("--- ").append(task.title()).append(" --- ")
                .append("#").append(task.tag().name());
        return line;
    }

}
