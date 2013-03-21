package net.sf.simplebacklog;

final class InProgressAppender implements TaskAppender<InProgress> {

    private final Appender parent;
    private final Appendable<InProgress> inProgressAppendable;

    public InProgressAppender(final Appender parent,
            final Appendable<InProgress> inProgressAppendable) {
        this.parent = parent;
        this.inProgressAppendable = inProgressAppendable;
    }

    @Override
    public void append(final InProgress... tasks) {
        for (final InProgress task : tasks) {
            parent().append(appendable().build(task)).newLine();
        }
        parent().newLine();
    }

    private Appendable<InProgress> appendable() {
        return this.inProgressAppendable;
    }

    private Appender parent() {
        return this.parent;
    }
}