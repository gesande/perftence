package net.sf.perftence.backlog;

import net.sf.simplebacklog.AppendableWaitingWithChalk;
import net.sf.simplebacklog.Backlog;
import net.sf.simplebacklog.BacklogAppender;
import net.sf.simplebacklog.BacklogDisplay;
import net.sf.simplebacklog.BacklogFactory;
import net.sf.simplebacklog.BacklogImpl;
import net.sf.simplebacklog.Chalk;
import net.sf.simplebacklog.ChalkBox;
import net.sf.simplebacklog.DefaultBacklogAppender;
import net.sf.simplebacklog.Done;
import net.sf.simplebacklog.InProgress;
import net.sf.simplebacklog.StringBuilderAppender;
import net.sf.simplebacklog.SysoutBacklogDisplay;
import net.sf.simplebacklog.TaskAppender;
import net.sf.simplebacklog.TaskList;
import net.sf.simplebacklog.TaskListFactory;
import net.sf.simplebacklog.Waiting;
import net.sf.simplebacklog.WaitingAppender;

public class BacklogWaitingForImplementation {

    public static void main(final String[] args) {
        new PerftenceBacklog(
                WaitingForImplementation
                        .displayedBy(new SysoutBacklogDisplay())).show();
    }

    private static class WaitingForImplementation implements BacklogFactory {

        private final BacklogDisplay display;

        public WaitingForImplementation(final BacklogDisplay display) {
            this.display = display;
        }

        @Override
        public Backlog newBacklog() {
            final ChalkBox chalkBox = new ChalkBox();
            final StringBuilderAppender appender = new StringBuilderAppender();
            final TaskAppender<Done> doneAppender = new TaskAppender<Done>() {
                @Override
                public void append(final Done... tasks) { //
                }
            };
            final TaskAppender<InProgress> inProgressAppender = new TaskAppender<InProgress>() {
                @Override
                public void append(final InProgress... tasks) { //
                }
            };

            final Chalk red = chalkBox.red();
            final TaskAppender<Waiting> waitingAppender = new WaitingAppender(
                    appender, new AppendableWaitingWithChalk(red));
            final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                    appender, doneAppender, inProgressAppender, waitingAppender);
            final TaskListFactory taskListFactory = new TaskListFactory() {

                @Override
                public TaskList<Backlog, Waiting> forWaiting(
                        final Backlog backlog) {
                    return new TaskList<Backlog, Waiting>() {

                        @Override
                        public Backlog tasks(final Waiting... tasks) {
                            backlogAppender.waiting(tasks);
                            return backlog;
                        }

                        @Override
                        public Backlog noTasks() {
                            return backlog;
                        }

                        @Override
                        public TaskList<Backlog, Waiting> title(
                                final String title) {
                            backlogAppender.subTitle(red.write(title));
                            return this;
                        }
                    };
                }

                @Override
                public TaskList<Backlog, InProgress> forInProgress(
                        final Backlog backlog) {
                    return new TaskList<Backlog, InProgress>() {

                        @Override
                        public TaskList<Backlog, InProgress> title(
                                final String title) {
                            return this;
                        }

                        @Override
                        public Backlog tasks(final InProgress... tasks) {
                            return backlog;
                        }

                        @Override
                        public Backlog noTasks() {
                            return backlog;
                        }
                    };
                }

                @Override
                public TaskList<Backlog, Done> forDone(final Backlog backlog) {
                    return new TaskList<Backlog, Done>() {

                        @Override
                        public TaskList<Backlog, Done> title(final String title) {
                            return this;
                        }

                        @Override
                        public Backlog tasks(final Done... tasks) {
                            return backlog;
                        }

                        @Override
                        public Backlog noTasks() {
                            return backlog;
                        }
                    };
                }
            };
            return new BacklogImpl(backlogAppender, display(), taskListFactory);
        }

        private BacklogDisplay display() {
            return this.display;
        }

        public static BacklogFactory displayedBy(final BacklogDisplay display) {
            return new WaitingForImplementation(display);
        }
    }
}
