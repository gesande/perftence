package net.sf.mybacklog;

public class WaitingAppender implements TaskAppender<Waiting> {

    private final Appender parent;
    private final Appendable<Waiting> appendableWaiting;

    public WaitingAppender(final Appender parent,
            final Appendable<Waiting> appendableWaiting) {
        this.parent = parent;
        this.appendableWaiting = appendableWaiting;
    }

    @Override
    public void append(final Waiting... tasks) {
        for (final Waiting task : tasks) {
            parent().append(appendable().build(task)).newLine();
        }
        parent().newLine();
    }

    private Appendable<Waiting> appendable() {
        return this.appendableWaiting;
    }

    private Appender parent() {
        return this.parent;
    }
}