package net.sf.simplebacklog;

final class DoneAsAppender implements AppenderAs<Done> {

    @Override
    public Appender task(final Done task) {
        final StringBuilderAppender line = new StringBuilderAppender();
        line.tab().append("+++ ").append(task.title()).append(" +++ ")
                .append("#").append(task.tag().name());
        return line;
    }

}
