package net.sf.perftence.backlog;

public abstract class AbstractBacklogging {
    @SuppressWarnings("static-method")
    protected final InProgress inProgress(final String title, final Tag tag) {
        return new InProgress() {

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
    protected final Waiting waiting(final String title, final Tag tag) {
        return new Waiting() {

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
    protected final Done done(final String title, final Tag tag) {
        return new Done() {

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
