package net.sf.simplebacklog;

@SuppressWarnings("static-method")
public final class ChalkedTaskAppender {

    public TaskAppender<Waiting> forWaiting(final Appender appender,
            final Chalk chalk) {
        return new WaitingAppender(appender, new ChalkedWaiting(chalk,
                new WaitingAsAppender()));
    }

    public TaskAppender<Done> forDone(final Appender appender, final Chalk chalk) {
        return new DoneAppender(appender, new ChalkedDone(chalk,
                new DoneAsAppender()));
    }

    public TaskAppender<InProgress> forInProgress(final Appender appender,
            final Chalk chalk) {
        return new InProgressAppender(appender, new ChalkedInProgress(chalk,
                new InProgressAsAppender()));
    }
}
