package net.sf.simplebacklog;

public interface Appendable<TASK extends Task> {
    String build(final TASK task);
}