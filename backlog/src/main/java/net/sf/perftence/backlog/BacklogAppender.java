package net.sf.perftence.backlog;

public interface BacklogAppender {

    void title(final String title);

    void subTitle(final String title);

    void done(final DoneTask... tasks);

    void waiting(final WaitingTask... tasks);

    void inProgress(final InProgressTask... tasks);

    String build();
}
