package net.sf.simplebacklog;

public interface Backlog {

    Backlog title(final String title);

    TaskList<Backlog, Done> done(final String title);

    TaskList<Backlog, Waiting> waiting(final String title);

    TaskList<Backlog, InProgress> inProgress(String string);

    void show();
}
