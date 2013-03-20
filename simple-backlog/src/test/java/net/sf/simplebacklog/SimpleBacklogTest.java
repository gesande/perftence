package net.sf.simplebacklog;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SimpleBacklogTest extends AbstractBacklogging {

    private MyBacklogDisplay display;

    @Before
    public void before() {
        this.display = new MyBacklogDisplay(new SysoutBacklogDisplay());
    }
    
    @Test
    public void simpleBacklog() {
        final Backlog newBacklog = newBacklog();

        newBacklog.title("simple backlog")

        .done().title("done:").tasks(done("first task", MyTag.first))

        .inProgress().title("in progress:")
                .tasks(inProgress("second task", MyTag.second))

                .waiting().title("waiting:")
                .tasks(waiting("third task", MyTag.third))

                .show();

        assertEquals(
                "simple backlog\n\n  done:\n\n  first task|first\n\n  in progress:\n\n  second task|second\n\n  waiting:\n\n  third task|third\n\n",
                this.display.result());
    }

    enum MyTag implements Tag {
        first, second, third
    }

    @Override
    protected Backlog newBacklog() {
        final Appender appender = new StringBuilderAppender();
        final TaskAppender<Done> doneAppender = new TaskAppender<Done>() {

            @Override
            public void append(final Done... tasks) {
                for (final Done task : tasks) {
                    appendTask(appender, task);
                }
                appender.newLine();
            }
        };

        final TaskAppender<InProgress> inProgressAppender = new TaskAppender<InProgress>() {

            @Override
            public void append(final InProgress... tasks) {
                for (final InProgress task : tasks) {
                    appendTask(appender, task);
                }
                appender.newLine();
            }
        };
        final TaskAppender<Waiting> waitingAppender = new TaskAppender<Waiting>() {

            @Override
            public void append(final Waiting... tasks) {
                for (final Waiting task : tasks) {
                    appendTask(appender, task);
                }
                appender.newLine();
            }
        };
        final DefaultBacklogAppender backlogAppender = new DefaultBacklogAppender(
                appender, doneAppender, inProgressAppender, waitingAppender);
        final DefaultTaskListFactory taskListFactory = new DefaultTaskListFactory(
                backlogAppender);
        return new Backlog() {

            @Override
            public TaskList<Backlog, Waiting> waiting() {
                return taskListFactory.forWaiting(this);
            }

            @Override
            public Backlog title(final String title) {
                backlogAppender.title(title);
                return this;
            }

            @Override
            public void show() {
                display().display(backlogAppender);
            }

            @Override
            public TaskList<Backlog, InProgress> inProgress() {
                return taskListFactory.forInProgress(this);
            }

            @Override
            public TaskList<Backlog, Done> done() {
                return taskListFactory.forDone(this);
            }
        };
    }

    private MyBacklogDisplay display() {
        return this.display;
    }

    private static class MyBacklogDisplay implements BacklogDisplay {

        private final BacklogDisplay display;
        private String result;

        public MyBacklogDisplay(final BacklogDisplay display) {
            this.display = display;
        }

        @Override
        public void display(final BacklogAppender appender) {
            this.result = appender.build();
            this.display.display(appender);
        }

        public String result() {
            return this.result;
        }
    }

    private static void appendTask(final Appender appender, final Task task) {
        appender.tab().append(task.title()).append("|")
                .append(task.tag().name()).newLine();
    }

}
