package net.sf.simplebacklog;

public interface AppenderAs<TASK extends Task> {
    Appender task(final TASK task);
}
