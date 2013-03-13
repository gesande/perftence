package net.sf.perftence.backlog;

interface TaskAppender<TASK extends Task> {
    void append(TASK... tasks);
}