package net.sf.perftence.backlog;

import java.util.ArrayList;
import java.util.List;

import net.sf.chalkbox.Chalk;
import net.sf.chalkbox.ChalkBox;
import net.sf.mybacklog.Backlog;
import net.sf.mybacklog.BacklogAppender;
import net.sf.mybacklog.BacklogDisplay;
import net.sf.mybacklog.BacklogFactory;
import net.sf.mybacklog.BacklogImpl;
import net.sf.mybacklog.ChalkedTaskAppender;
import net.sf.mybacklog.DefaultBacklogAppender;
import net.sf.mybacklog.Done;
import net.sf.mybacklog.InProgress;
import net.sf.mybacklog.StringBuilderAppender;
import net.sf.mybacklog.SysoutBacklogDisplay;
import net.sf.mybacklog.Tag;
import net.sf.mybacklog.TaskList;
import net.sf.mybacklog.TaskListFactory;
import net.sf.mybacklog.Waiting;

public class BacklogWaitingForImplementation {

    public static void main(final String[] args) {
        final List<Tag> tags = toTags(args);
        new PerftenceBacklog(WaitingForImplementation.displayedBy(
                new SysoutBacklogDisplay(), tags)).show();
    }

    private static List<Tag> toTags(final String[] args) {
        final List<Tag> tags = new ArrayList<Tag>();
        if (args.length > 0) {
            for (final String value : args) {
                tags.add(PerftenceTag.valueOf(value));
            }
        } else {
            for (final Tag tag : PerftenceTag.values()) {
                tags.add(tag);
            }
        }
        return tags;
    }

    private static class WaitingForImplementation implements BacklogFactory {

        private final BacklogDisplay display;
        private final List<Tag> waitingTags = new ArrayList<Tag>();

        public WaitingForImplementation(final BacklogDisplay display,
                final List<Tag> waitingTags) {
            this.display = display;
            waitingTasks().addAll(waitingTags);
        }

        Waiting[] filtered(final Waiting[] tasks) {
            List<Waiting> filtered = new ArrayList<Waiting>();
            for (Waiting task : tasks) {
                if (waitingTasks().contains(task.tag())) {
                    filtered.add(task);
                }
            }
            return filtered.toArray(new Waiting[] {});
        }

        private List<Tag> waitingTasks() {
            return this.waitingTags;
        }

        @Override
        public Backlog newBacklog() {
            final ChalkBox chalkBox = new ChalkBox();
            final StringBuilderAppender appender = new StringBuilderAppender();
            final ChalkedTaskAppender chalkedTaskAppender = new ChalkedTaskAppender();
            final TaskAppenderDoingNothing doingNothing = new TaskAppenderDoingNothing();
            final Chalk red = chalkBox.red();
            final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                    appender, doingNothing.forDone(),
                    doingNothing.forInProgress(),
                    chalkedTaskAppender.forWaiting(appender, red));
            final TaskListFactory taskListFactory = new TaskListFactory() {

                @Override
                public TaskList<Backlog, Waiting> forWaiting(
                        final Backlog backlog) {
                    return new TaskList<Backlog, Waiting>() {

                        @Override
                        public Backlog tasks(final Waiting... tasks) {
                            backlogAppender.waiting(filtered(tasks));
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

        public static BacklogFactory displayedBy(final BacklogDisplay display,
                final List<Tag> waitingTags) {
            return new WaitingForImplementation(display, waitingTags);
        }
    }
}
