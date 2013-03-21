package net.sf.simplebacklog;

public final class ChalkedDone implements Appendable<Done> {
    private final Chalk chalk;
    private final AppenderAs<Done> appendable;

    public ChalkedDone(final Chalk chalk, final AppenderAs<Done> appendable) {
        this.chalk = chalk;
        this.appendable = appendable;
    }

    @Override
    public String build(final Done task) {
        return chalk().write(appendable().task(task).build());
    }

    private AppenderAs<Done> appendable() {
        return this.appendable;
    }

    private Chalk chalk() {
        return this.chalk;
    }
}