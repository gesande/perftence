package net.sf.simplebacklog;

public interface TaskList<RETURN, TASK> {

    RETURN tasks(TASK... tasks);

    RETURN noTasks();
}
