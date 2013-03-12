package net.sf.perftence.backlog;

public abstract class AbstractBacklogging {
    @SuppressWarnings("static-method")
    protected final InProgressTask inProgress(final String title, final Tag tag) {
        return new InProgressTask() {

            @Override
            public String title() {
                return title;
            }

            @Override
            public Tag tag() {
                return tag;
            }
        };

    }

    @SuppressWarnings("static-method")
    protected final WaitingTask waiting(final String title, final Tag tag) {
        return new WaitingTask() {

            @Override
            public String title() {
                return title;
            }

            @Override
            public Tag tag() {
                return tag;
            }
        };

    }

    @SuppressWarnings("static-method")
    protected final DoneTask done(final String title, final Tag tag) {
        return new DoneTask() {

            @Override
            public String title() {
                return title;
            }

            @Override
            public Tag tag() {
                return tag;
            }
        };

    }

    protected abstract Backlog backlog();

}
