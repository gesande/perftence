package net.sf.simplebacklog;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultBacklogAppenderTest {
    private TaskFactory taskFactory;

    public DefaultBacklogAppenderTest() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void inProgress() {
        final BacklogAppender backlogAppender = new DefaultBacklogAppender();
        final Tag tag = new Tag() {

            @Override
            public String name() {
                return "inprogress tag";
            }
        };
        backlogAppender.title("title");
        backlogAppender.inProgress(taskFactory().inProgress("inprogress", tag));
        String result = "title\n\n" + "      inprogress     #inprogress tag\n";

        assertEquals(result, backlogAppender.build());
    }

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }
}
