package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.TestFailureNotifier;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AgentBasedTestTest {
    private final static Logger LOG = LoggerFactory
            .getLogger(AgentBasedTestTest.class);
    private boolean testFailed = false;
    private Throwable testFailure;
    private AtomicInteger taskRun = new AtomicInteger();

    @Before
    public void before() {
        taskRun().set(0);
    }

    @Test
    public void sanityCheck() {
        final TestBuilder test = new AgentBasedTest(new MyFailureTestNotifier())
                .test("sanityCheck");
        assertNotNull(
                "Uuh, null returned by agent based test.test(id) method!", test);
        assertEquals("Id doesn't match!", "sanityCheck", test.id());
        test.agents(failingAgent()).start();
        assertTrue(this.testFailed);
        assertTrue(this.testFailure.getClass().equals(MyFailure.class));
        assertEquals(1, taskRun().get());
    }

    @Test
    public void noInvocationGraph() {
        final TestBuilder test = new AgentBasedTest(new MyFailureTestNotifier())
                .test("noInvocationGraph").noInvocationGraph();
        assertNotNull(
                "Uuh, null returned by agent based test.test(id) method!", test);
        assertEquals("Id doesn't match!", "noInvocationGraph", test.id());
        test.agents(failingAgent()).start();
        assertTrue(this.testFailed);
        assertTrue(this.testFailure.getClass().equals(MyFailure.class));
        assertEquals(1, taskRun().get());
    }

    @Test
    public void oneAgentOneTask() {
        new AgentBasedTest(new TestingNotifier()).test("oneAgentOneTask")
                .agents(agentsWithOneTask(1)).latencyGraphFor(Category.One)
                .start();
        assertFalse(this.testFailed);
        assertEquals(1, taskRun().get());
    }

    @Test
    public void manyAgentsOneTask() {
        new AgentBasedTest(new TestingNotifier()).test("manyAgentsOneTask")
                .agents(agentsWithOneTask(5)).latencyGraphFor(Category.One)
                .start();
        assertFalse(this.testFailed);
        assertEquals(5, taskRun().get());
    }

    @Test
    public void oneAgentManyTasks() {
        new AgentBasedTest(new TestingNotifier()).test("oneAgentManyTasks")
                .agents(agentsWithManyTasks(1)).latencyGraphFor(Category.One)
                .start();
        assertFalse(this.testFailed);
        assertEquals(2, taskRun().get());
    }

    @Test
    public void manyAgentsManyTasks() {
        new AgentBasedTest(new TestingNotifier()).test("manyAgentsManyTasks")
                .agents(agentsWithManyTasks(2)).latencyGraphFor(Category.One)
                .start();
        assertFalse(this.testFailed);
        assertEquals(4, taskRun().get());
    }

    @Test
    public void manyAgentsManyTasksLatencyGraphForCategoryOne() {
        new AgentBasedTest(new TestingNotifier()).test("manyAgentsManyTasks")
                .agents(agentsWithManyTasks(2)).latencyGraphFor(Category.One)
                .start();
        assertFalse(this.testFailed);
        assertEquals(4, taskRun().get());
    }

    @Test
    public void manyAgentsManyTasksLatencyGraphForAll() {
        new AgentBasedTest(new TestingNotifier()).test("manyAgentsManyTasks")
                .agents(agentsWithManyTasks(2)).latencyGraphForAll().start();
        assertFalse(this.testFailed);
        assertEquals(4, taskRun().get());
    }

    @SuppressWarnings({ "static-method", "unused" })
    @Test(expected = TestFailureNotifier.NoTestNotifierException.class)
    public void nullNotifier() {
        new AgentBasedTest(null);
    }

    private Collection<TestAgent> agentsWithManyTasks(final int agentCount) {
        final List<TestAgent> agents = new ArrayList<TestAgent>();
        for (int i = 0; i < agentCount; i++) {
            agents.add(new AgentWithManyTasks());
        }
        return agents;
    }

    class AgentWithManyTasks implements TestAgent {

        @Override
        public TestTask firstTask() {
            return newTestTask(
                    TimeSpecificationFactory.now(),
                    100,
                    Category.One,
                    newTestTask(TimeSpecificationFactory.inMillis(100), 50,
                            Category.Two, null));
        }
    }

    private TestTask newTestTask(final Time scheduled, final int sleep,
            final TestTaskCategory category, final TestTask next) {
        return new TestTask() {

            @Override
            public Time when() {
                return scheduled;
            }

            @Override
            public void run(final TestTaskReporter reporter) throws Exception {
                Thread.sleep(sleep);
                LOG.info("Time spent so far = " + reporter.timeSpentSoFar());
                taskRun().incrementAndGet();

            }

            @Override
            public TestTask nextTaskIfAny() {
                return next;
            }

            @Override
            public TestTaskCategory category() {
                return category;
            }
        };
    }

    private Collection<TestAgent> agentsWithOneTask(final int agentCount) {
        final List<TestAgent> agents = new ArrayList<TestAgent>();
        for (int i = 0; i < agentCount; i++) {
            agents.add(new AgentWithOneTask());
        }
        return agents;
    }

    class AgentWithOneTask implements TestAgent {

        @Override
        public TestTask firstTask() {
            return new TestTask() {

                @Override
                public Time when() {
                    return TimeSpecificationFactory
                            .someMillisecondsFromNow(1500);
                }

                @Override
                public void run(TestTaskReporter reporter) throws Exception {
                    Thread.sleep(100);
                    LOG.info("Time spent so far = " + reporter.timeSpentSoFar());
                    taskRun().incrementAndGet();
                }

                @Override
                public TestTask nextTaskIfAny() {
                    return null;
                }

                @Override
                public TestTaskCategory category() {
                    return Category.One;
                }
            };
        }
    }

    class TestingNotifier implements TestFailureNotifier {

        @Override
        public void testFailed(Throwable t) {
            testFailureOf(t);
        }

    }

    private void testFailureOf(final Throwable t) {
        this.testFailed = true;
        this.testFailure = t;
    }

    private Collection<TestAgent> failingAgent() {
        final List<TestAgent> agents = new ArrayList<TestAgent>();
        agents.add(new FailingAgent());
        return agents;
    }

    private class FailingAgent implements TestAgent {

        @Override
        public TestTask firstTask() {
            return new TestTask() {

                @Override
                public Time when() {
                    return TimeSpecificationFactory.now();
                }

                @Override
                public void run(final TestTaskReporter reporter)
                        throws Exception {
                    Thread.sleep(50);
                    log().info(
                            "Time spent so far = " + reporter.timeSpentSoFar());
                    taskRun().incrementAndGet();
                }

                @Override
                public TestTask nextTaskIfAny() {
                    return new TestTask() {

                        @Override
                        public Time when() {
                            return TimeSpecificationFactory.now();
                        }

                        @Override
                        public void run(TestTaskReporter reporter)
                                throws Exception {
                            throw new MyFailure("I fail!");
                        }

                        @Override
                        public TestTask nextTaskIfAny() {
                            return null;
                        }

                        @Override
                        public TestTaskCategory category() {
                            return Category.Fail;
                        }
                    };
                }

                @Override
                public TestTaskCategory category() {
                    return Category.One;
                }
            };
        }
    }

    private static class MyFailure extends Exception {

        public MyFailure(final String message) {
            super(message);
        }
    }

    private enum Category implements TestTaskCategory {
        One, Fail, Two
    }

    private class MyFailureTestNotifier implements TestFailureNotifier {
        @Override
        public void testFailed(final Throwable t) {
            testFailureOf(t);
            assertTrue(t.getClass().equals(MyFailure.class));
        }
    }

    private AtomicInteger taskRun() {
        return this.taskRun;
    }

    private static Logger log() {
        return LOG;
    }
}
