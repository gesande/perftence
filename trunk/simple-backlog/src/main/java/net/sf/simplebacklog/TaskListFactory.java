package net.sf.simplebacklog;

public interface TaskListFactory {

    TaskList<Backlog, Done> forDone(final Backlog backlog);

    TaskList<Backlog, Waiting> forWaiting(final Backlog backlog);

    TaskList<Backlog, InProgress> forInProgress(final Backlog backlog);

}
