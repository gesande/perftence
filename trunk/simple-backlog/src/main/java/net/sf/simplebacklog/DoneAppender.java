package net.sf.simplebacklog;

class DoneAppender implements TaskAppender<Done> {

    private final Appender parent;
    private final Appendable<Done> doneAppendable;

    public DoneAppender(final Appender parent,
            final Appendable<Done> doneAppendable) {
        this.parent = parent;
        this.doneAppendable = doneAppendable;
    }

    @Override
    public void append(final Done... tasks) {
        for (final Done task : tasks) {
            parent().append(appendable().build(task)).newLine();
        }
        parent().newLine();
    }

    private Appendable<Done> appendable() {
        return this.doneAppendable;
    }

    private Appender parent() {
        return this.parent;
    }
}