package net.sf.mybacklog;

import static org.junit.Assert.assertEquals;

import net.sf.mybacklog.Backlog;
import net.sf.mybacklog.BacklogAppender;
import net.sf.mybacklog.BacklogDisplay;
import net.sf.mybacklog.BacklogFactoryUsingChalks;
import net.sf.mybacklog.Done;
import net.sf.mybacklog.InProgress;
import net.sf.mybacklog.SysoutBacklogDisplay;
import net.sf.mybacklog.Tag;
import net.sf.mybacklog.TaskFactory;
import net.sf.mybacklog.Waiting;

import org.junit.Before;
import org.junit.Test;

public class BacklogFactoryUsingChalksTest {

    private MyBacklogDisplay display;
    private TaskFactory taskFactory;

    @Before
    public void before() {
        this.display = new MyBacklogDisplay(new SysoutBacklogDisplay());
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void noTasks() {
        final Backlog newBacklog = BacklogFactoryUsingChalks.displayedBy(
                display()).newBacklog();
        newBacklog.title("no tasks").done().noTasks().inProgress().noTasks()
                .waiting().noTasks().show();

        assertEquals("no tasks\n\n", display().result());
    }

    @Test
    public void withSysoutAndChalks() {
        final Backlog newBacklog = BacklogFactoryUsingChalks.displayedBy(
                display()).newBacklog();

        newBacklog.title("simple backlog")

        .done().title("done:").tasks(done("first task", MyTag.first))

        .inProgress().title("in progress:")
                .tasks(inProgress("second task", MyTag.second))

                .waiting().title("waiting:")
                .tasks(waiting("third task", MyTag.third))

                .show();
        assertEquals(
                "simple backlog\n\n  [32mdone:[m\n\n[32m  +++ first task +++ #first[m\n\n  [33min progress:[m\n\n[33m      second task     #second[m\n\n  [31mwaiting:[m\n\n[31m  --- third task --- #third[m\n\n",
                display().result());

    }

    private Waiting waiting(final String title, Tag tag) {
        return taskFactory().waiting(title, tag);
    }

    private InProgress inProgress(String title, Tag tag) {
        return taskFactory().inProgress(title, tag);
    }

    private Done done(final String title, final Tag tag) {
        return taskFactory().done(title, tag);
    }

    enum MyTag implements Tag {
        first, second, third
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

    private MyBacklogDisplay display() {
        return this.display;
    }

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }
}
