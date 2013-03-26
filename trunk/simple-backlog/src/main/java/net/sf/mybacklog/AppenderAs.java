package net.sf.mybacklog;

public interface AppenderAs<TASK extends Task> {
    Appender task(final TASK task);
}
