package net.sf.perftence.backlog;

import java.util.ArrayList;
import java.util.List;

import net.sf.simplebacklog.Backlog;
import net.sf.simplebacklog.BacklogAppender;
import net.sf.simplebacklog.BacklogDisplay;
import net.sf.simplebacklog.BacklogFactory;
import net.sf.simplebacklog.BacklogImpl;
import net.sf.simplebacklog.ChalkBox;
import net.sf.simplebacklog.ChalkedTaskAppender;
import net.sf.simplebacklog.DefaultBacklogAppender;
import net.sf.simplebacklog.Done;
import net.sf.simplebacklog.InProgress;
import net.sf.simplebacklog.StringBuilderAppender;
import net.sf.simplebacklog.SysoutBacklogDisplay;
import net.sf.simplebacklog.Tag;
import net.sf.simplebacklog.TaskAppender;
import net.sf.simplebacklog.TaskList;
import net.sf.simplebacklog.TaskListFactory;
import net.sf.simplebacklog.Waiting;

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

        private Waiting[] filtered(final Waiting[] tasks) {
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

            final TaskAppender<Waiting> waitingAppender = chalkedTaskAppender
                    .forWaiting(appender, chalkBox.red());
            final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                    appender, doneAppender, inProgressAppender, waitingAppender);
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
                            backlogAppender.subTitle(chalkBox.red()
                                    .write(title));
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
