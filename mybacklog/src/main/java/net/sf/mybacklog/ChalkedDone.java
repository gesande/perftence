package net.sf.mybacklog;

import net.sf.chalkbox.Chalk;

final class ChalkedDone implements Appendable<Done> {
    private final Chalk chalk;
    private final AppenderAs<Done> appendable;

    public ChalkedDone(final Chalk chalk, final AppenderAs<Done> appendable) {
        this.chalk = chalk;
        this.appendable = appendable;
    }

    @Override
    public String build(final Done task) {
        return chalk().write(appenderAs().task(task).build());
    }

    private AppenderAs<Done> appenderAs() {
        return this.appendable;
    }

    private Chalk chalk() {
        return this.chalk;
    }
}