package net.sf.perftence.backlog;

import net.sf.simplebacklog.Done;
import net.sf.simplebacklog.InProgress;
import net.sf.simplebacklog.TaskAppender;

final class TaskAppenderDoingNothing {

    @SuppressWarnings("static-method")
    public TaskAppender<Done> forDone() {
        return new TaskAppender<Done>() {
            @Override
            public void append(final Done... tasks) {
                // doing as promised i.e. nothing
            }
        };
    }

    @SuppressWarnings("static-method")
    public TaskAppender<InProgress> forInProgress() {
        return new TaskAppender<InProgress>() {
            @Override
            public void append(final InProgress... tasks) {
                // doing as promised i.e. nothing
            }
        };
    }

}
