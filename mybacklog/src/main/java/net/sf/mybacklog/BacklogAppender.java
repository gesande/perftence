package net.sf.mybacklog;

public interface BacklogAppender {

    void title(final String title);

    void subTitle(final String title);

    void done(final Done... tasks);

    void waiting(final Waiting... tasks);

    void inProgress(final InProgress... tasks);

    String build();
}
