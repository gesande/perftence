package net.sf.perftence.backlog;

public interface TaskList<RETURN, TASK> {

    RETURN tasks(TASK... tasks);

    RETURN noTasks();
}
