package net.sf.mybacklog;

final class InProgressAsAppender implements AppenderAs<InProgress> {

    @Override
    public Appender task(final InProgress task) {
        final StringBuilderAppender line = new StringBuilderAppender();
        line.tab().append("    ").append(task.title()).append("     ")
                .append("#").append(task.tag().name());
        return line;
    }

}
