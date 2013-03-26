package net.sf.mybacklog;

public interface TaskList<RETURN, TASK> {

    TaskList<RETURN, TASK> title(final String title);

    RETURN tasks(TASK... tasks);

    RETURN noTasks();
}
