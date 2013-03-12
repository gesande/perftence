package net.sf.perftence.backlog;

public interface Backlog {

    Backlog title(final String title);

    TaskList<Backlog, DoneTask> done(final String title);

    TaskList<Backlog, WaitingTask> waiting(final String title);

    TaskList<Backlog, InProgressTask> inProgress(String string);

    void show();
}
