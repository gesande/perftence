package net.sf.simplebacklog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DefaultBacklogAppenderTest {
    private TaskFactory taskFactory;

    public DefaultBacklogAppenderTest() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void inProgress() {
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
        backlogAppender.inProgress(taskFactory().inProgress("inprogress", tag));
        assertEquals("  inprogressinprogress tag\n", backlogAppender.build());
    }

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }
}
