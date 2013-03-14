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
        String result = "title\n\n"
                + "[33m      inprogress     #inprogress tag\n[m\n\n";

        final String build = backlogAppender.build();
        System.out.println(build);
        assertEquals(result, build);
    }

    private TaskFactory taskFactory() {
        return this.taskFactory;
    }
}
