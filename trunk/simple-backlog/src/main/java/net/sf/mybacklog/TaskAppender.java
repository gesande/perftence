package net.sf.mybacklog;

public interface TaskAppender<TASK extends Task> {
    void append(TASK... tasks);
}