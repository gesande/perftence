package net.sf.simplebacklog;

public interface TaskAppender<TASK extends Task> {
    void append(TASK... tasks);
}