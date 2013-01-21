package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.perftence.TestFailureNotifier;

import org.junit.Test;

public final class AgentBasedTestTest implements TestFailureNotifier {

    private boolean testFailed = false;
    private Throwable testFailure;

    @Test
    public void sanityCheck() {
        final TestBuilder test = new AgentBasedTest(this).test("id");
        assertNotNull(
                "Uuh, null returned by agent based test.test(id) method!", test);
        assertEquals("Id doesn't match!", "id", test.id());
        test.agents(oneAgent()).start();
        assertTrue(this.testFailed);
        assertTrue(this.testFailure.getClass().equals(MyFailure.class));
    }

    @Test
    public void noInvocationGraph() {
        final TestBuilder test = new AgentBasedTest(this).test("id")
                .noInvocationGraph();
        assertNotNull(
                "Uuh, null returned by agent based test.test(id) method!", test);
        assertEquals("Id doesn't match!", "id", test.id());
        test.agents(oneAgent()).start();
        assertTrue(this.testFailed);
        assertTrue(this.testFailure.getClass().equals(MyFailure.class));

    }

    private static Collection<TestAgent> oneAgent() {
        final List<TestAgent> agents = new ArrayList<TestAgent>();
        agents.add(new Agent());
        return agents;
    }

    private static class Agent implements TestAgent {

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
                    Thread.sleep(100);
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
        One, Fail
    }

    @Override
    public void testFailed(Throwable t) {
        this.testFailed = true;
        this.testFailure = t;
        assertTrue(t.getClass().equals(MyFailure.class));
    }
}
