package net.sf.perftence.backlog;

import net.sf.mybacklog.Done;
import net.sf.mybacklog.InProgress;
import net.sf.mybacklog.TaskAppender;

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
