package net.sf.simplebacklog;

interface TaskAppender<TASK extends Task> {
    void append(TASK... tasks);
}