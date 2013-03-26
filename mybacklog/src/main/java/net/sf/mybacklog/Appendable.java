package net.sf.mybacklog;

public interface Appendable<TASK extends Task> {
    String build(final TASK task);
}