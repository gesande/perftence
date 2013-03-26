package net.sf.mybacklog;

public interface Backlog {

    Backlog title(final String title);

    TaskList<Backlog, Done> done();

    TaskList<Backlog, Waiting> waiting();

    TaskList<Backlog, InProgress> inProgress();

    void show();
}
