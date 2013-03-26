package net.sf.mybacklog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import net.sf.mybacklog.BacklogAppender;
import net.sf.mybacklog.DefaultBacklogAppender;
import net.sf.mybacklog.Done;
import net.sf.mybacklog.InProgress;
import net.sf.mybacklog.StringBuilderAppender;
import net.sf.mybacklog.SysoutBacklogDisplay;
import net.sf.mybacklog.Tag;
import net.sf.mybacklog.TaskAppender;
import net.sf.mybacklog.TaskFactory;
import net.sf.mybacklog.Waiting;

import org.junit.Test;

public class DefaultBacklogAppenderTest {
    private TaskFactory taskFactory;

    public DefaultBacklogAppenderTest() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void done() {
        final SysoutBacklogDisplay sysoutDisplay = new SysoutBacklogDisplay();
        final StringBuilderAppender appender = new StringBuilderAppender();
        final TaskAppender<Done> doneAppender = new TaskAppender<Done>() {
            @Override
            public void append(Done... tasks) {
                assertNotNull(tasks);
                assertEquals(1, tasks.length);
                appender.tab().append(tasks[0].title())
                        .append(tasks[0].tag().name()).newLine();
            }
        };
        final TaskAppender<InProgress> inProgressAppender = new TaskAppender<InProgress>() {
            @Override
            public void append(InProgress... tasks) {
                fail("Should not come here!");
            }
        };
        final TaskAppender<Waiting> waitingAppender = new TaskAppender<Waiting>() {
            @Override
            public void append(Waiting... tasks) {
                fail("Should not come here!");
            }
        };
        final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                appender, doneAppender, inProgressAppender, waitingAppender);
        final Tag tag = new Tag() {
            @Override
            public String name() {
                return "done tag";
            }
        };
        backlogAppender.title("main title");
        backlogAppender.subTitle("done title");

        backlogAppender.done(taskFactory().done("done", tag));
        sysoutDisplay.display(backlogAppender);
        assertEquals("main title\n\n  done title\n\n  donedone tag\n",
                backlogAppender.build());

    }

    @Test
    public void inProgress() {
        final SysoutBacklogDisplay sysoutDisplay = new SysoutBacklogDisplay();
        final StringBuilderAppender appender = new StringBuilderAppender();
        final TaskAppender<Done> doneAppender = new TaskAppender<Done>() {
            @Override
            public void append(Done... tasks) {
                fail("Should not come here!");
            }
        };
        final TaskAppender<InProgress> inProgressAppender = new TaskAppender<InProgress>() {
            @Override
            public void append(InProgress... tasks) {
                assertNotNull(tasks);
                assertEquals(1, tasks.length);
                appender.tab().append(tasks[0].title())
                        .append(tasks[0].tag().name()).newLine();
            }
        };
        final TaskAppender<Waiting> waitingAppender = new TaskAppender<Waiting>() {
            @Override
            public void append(Waiting... tasks) {
                fail("Should not come here!");
            }
        };
        final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                appender, doneAppender, inProgressAppender, waitingAppender);
        final Tag tag = new Tag() {
            @Override
            public String name() {
                return "inprogress tag";
            }
        };
        backlogAppender.title("main title");
        backlogAppender.subTitle("inprogress title");
        backlogAppender.inProgress(taskFactory().inProgress("inprogress", tag));
        sysoutDisplay.display(backlogAppender);
        assertEquals(
                "main title\n\n  inprogress title\n\n  inprogressinprogress tag\n",
                backlogAppender.build());
    }

    @Test
    public void waiting() {
        final SysoutBacklogDisplay sysoutDisplay = new SysoutBacklogDisplay();
        final StringBuilderAppender appender = new StringBuilderAppender();
        final TaskAppender<Waiting> waitingAppender = new TaskAppender<Waiting>() {
            @Override
            public void append(Waiting... tasks) {
                assertNotNull(tasks);
                assertEquals(1, tasks.length);
                appender.tab().append(tasks[0].title())
                        .append(tasks[0].tag().name()).newLine();
            }
        };
        final TaskAppender<InProgress> inProgressAppender = new TaskAppender<InProgress>() {
            @Override
            public void append(InProgress... tasks) {
                fail("Should not come here!");
            }
        };
        final TaskAppender<Done> doneAppender = new TaskAppender<Done>() {
            @Override
            public void append(Done... tasks) {
                fail("Should not come here!");
            }
        };
        final BacklogAppender backlogAppender = new DefaultBacklogAppender(
                appender, doneAppender, inProgressAppender, waitingAppender);
        final Tag tag = new Tag() {
            @Override
            public String name() {
                return "waiting tag";
            }
        };
        backlogAppender.title("main title");
        backlogAppender.subTitle("waiting title");
        backlogAppender.waiting(taskFactory().waiting("waiting", tag));
        sysoutDisplay.display(backlogAppender);
        assertEquals("main title\n\n  waiting title\n\n  waitingwaiting tag\n",
                backlogAppender.build());
    }

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }
}
