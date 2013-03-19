package net.sf.simplebacklog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BacklogImplTest {

    @SuppressWarnings("static-method")
    @Test
    public void noTasks() {
        final List<String> subtitles = new ArrayList<String>();
        final BacklogAppender backlogAppender = new BacklogAppender() {
            @Override
            public void waiting(Waiting... tasks) {
                fail("You should not have come here!");
            }

            @Override
            public void title(String title) {
                assertEquals("title", title);
            }

            @Override
            public void subTitle(String title) {
                subtitles.add(title);
            }

            @Override
            public void inProgress(InProgress... tasks) {
                fail("You should not have come here!");
            }

            @Override
            public void done(Done... tasks) {
                fail("You should not have come here!");
            }

            @Override
            public String build() {
                return "build";
            }
        };
        final BacklogDisplay display = new BacklogDisplay() {

            @Override
            public void display(final BacklogAppender appender) {
                assertNotNull(appender);
                assertEquals(backlogAppender, appender);
                assertEquals("build", appender.build());
            }
        };
        new BacklogImpl(backlogAppender, display, new DefaultTaskListFactory(
                backlogAppender)).title("title").done().title("done").noTasks()
                .inProgress().title("inprogress").noTasks().waiting()
                .title("waiting").noTasks().show();
        assertEquals(3, subtitles.size());
        assertEquals("done", subtitles.get(0));
        assertEquals("inprogress", subtitles.get(1));
        assertEquals("waiting", subtitles.get(2));
    }

    @SuppressWarnings("static-method")
    @Test
    public void backlogWithTasks() {
        final List<String> subtitles = new ArrayList<String>();
        final Tag waiting = new Tag() {

            @Override
            public String name() {
                return "waiting";
            }
        };
        final Tag inProgress = new Tag() {

            @Override
            public String name() {
                return "inprogress";
            }
        };
        final Tag done = new Tag() {

            @Override
            public String name() {
                return "done";
            }
        };
        final BacklogAppender backlogAppender = new BacklogAppender() {

            @Override
            public void waiting(final Waiting... tasks) {
                assertEquals(1, tasks.length);
                assertEquals("waiting", tasks[0].title());
                assertEquals(waiting, tasks[0].tag());
            }

            @Override
            public void title(final String title) {
                assertEquals("title", title);
            }

            @Override
            public void subTitle(final String title) {
                subtitles.add(title);
            }

            @Override
            public void inProgress(final InProgress... tasks) {
                assertEquals(1, tasks.length);
                assertEquals("inprogress", tasks[0].title());
                assertEquals(inProgress, tasks[0].tag());
            }

            @Override
            public void done(final Done... tasks) {
                assertEquals(1, tasks.length);
                assertEquals("done", tasks[0].title());
                assertEquals(done, tasks[0].tag());
            }

            @Override
            public String build() {
                return "build";
            }
        };
        final BacklogDisplay display = new BacklogDisplay() {

            @Override
            public void display(final BacklogAppender appender) {
                assertEquals(backlogAppender, appender);
                assertEquals("build", appender.build());
            }
        };
        new BacklogImpl(backlogAppender, display, new DefaultTaskListFactory(
                backlogAppender)).title("title").done().title("done")
                .tasks(new Done() {

                    @Override
                    public String title() {
                        return "done";
                    }

                    @Override
                    public Tag tag() {
                        return done;
                    }
                }).inProgress().title("inProgress").tasks(new InProgress() {

                    @Override
                    public String title() {
                        return "inprogress";
                    }

                    @Override
                    public Tag tag() {
                        return inProgress;
                    }
                }).waiting().title("waiting").tasks(new Waiting() {

                    @Override
                    public String title() {
                        return "waiting";
                    }

                    @Override
                    public Tag tag() {
                        return waiting;
                    }
                })

                .show();

        assertEquals(3, subtitles.size());
        assertEquals("done", subtitles.get(0));
        assertEquals("inProgress", subtitles.get(1));
        assertEquals("waiting", subtitles.get(2));
    }
}
