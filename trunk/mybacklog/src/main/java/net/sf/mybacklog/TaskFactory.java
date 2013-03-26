package net.sf.mybacklog;

final class TaskFactory {
    @SuppressWarnings("static-method")
    public InProgress inProgress(final String title, final Tag tag) {
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
    public Waiting waiting(final String title, final Tag tag) {
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
    public Done done(final String title, final Tag tag) {
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

}
