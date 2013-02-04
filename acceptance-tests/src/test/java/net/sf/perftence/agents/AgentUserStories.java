package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(DefaultTestRunner.class)
public class AgentUserStories extends AbstractMultiThreadedTest {

    private final static Logger LOG = LoggerFactory
            .getLogger(AgentUserStories.class);

    @Test
    public void concurrentStoryWithSpecifiedLatencyGraph() {
        agentBasedTest().agents(agents(2, new ConcurrentAgentFactory()))
                .latencyGraphFor(TestCategory.AliveAgent).start();
    }

    @Test
    public void oneAgentOneTask() {
        agentBasedTest().workerWaitTime(50).invocationRange(100)
                .throughputRange(6500)
                .agents(agents(100000, new SleepingMs(40, 50))).workers(1000)
                .start();
    }

    @Test
    public void sleepingAgentStoryWithTasksThatAreScheduledToBeRunNow() {
        agentBasedTest()
                .workers(10000)
                .agents(agents(10000,
                        new SleepingTestAgentFactoryWithNowFlavour())).start();
    }

    @Test
    public void sleepingAgentStoryWithTwoTasks() {
        agentBasedTest()
                .agents(agents(
                        10000,
                        new SleepingTestAgentFactoryWithNowFlavourHavingNextTask()))
                .workers(2000).start();
    }

    @Test
    public void agentBasedTestWithAllowedExceptions() {
        agentBasedTest().agents(failingAgents(5000)).allow(Fail.class).start();
    }

    @Test
    public void noInvocationGraphForOverallStatistics() {
        agentBasedTest().noInvocationGraph().workerWaitTime(50)
                .invocationRange(100).throughputRange(6500)
                .agents(agents(100000, new SleepingMs(40, 50))).workers(1000)
                .start();

    }

    class SleepingTestAgentFactoryWithNowFlavour implements TestAgentFactory {

        @Override
        public TestAgent newTestAgent(final int id) {
            return new TestAgentWithNowFlavour();
        }

        class TestAgentWithNowFlavour implements TestAgent {

            public TestAgentWithNowFlavour() {
            }

            @Override
            public TestTask firstTask() {
                return newTask(0, 100, null);
            }
        }
    }

    class SleepingTestAgentFactoryWithNowFlavourHavingNextTask implements
            TestAgentFactory {

        @Override
        public TestAgent newTestAgent(int id) {
            return new TestAgentWithTwoTasks();
        }

        class TestAgentWithTwoTasks implements TestAgent {
            @Override
            public TestTask firstTask() {
                return newTask(0, 100, newTask(0, 100, null));
            }
        }
    }

    class Fail extends Exception {

        public Fail(final String message) {
            super(message);
        }
    }

    private static TestTask newTask(final int scheduled, final int sleep,
            final TestTask next) {
        return new TestTask() {

            @Override
            public Time when() {
                return TimeSpecificationFactory
                        .someMillisecondsFromNow(scheduled);
            }

            @Override
            public void run(TestTaskReporter reporter) throws Exception {
                Thread.sleep(sleep);
            }

            @Override
            public TestTask nextTaskIfAny() {
                return next;
            }

            @Override
            public TestTaskCategory category() {
                return TestCategory.AliveAgent;
            }
        };
    }

    private Collection<TestAgent> failingAgents(int agents) {
        final List<TestAgent> list = new ArrayList<TestAgent>();
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < agents; i++) {
            final TestTask first = (counter.get() % 10 == 0) ? log(new FailTask(
                    1000)) : newTask(50, 100, null);
            counter.incrementAndGet();
            list.add(new FailingHalfTheTime(first));
        }
        return list;
    }

    private static Collection<TestAgent> agents(final int initialCapacity,
            final TestAgentFactory factory) {
        final List<TestAgent> agents = new ArrayList<TestAgent>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            agents.add(factory.newTestAgent(i));
        }
        return agents;
    }

    interface TestAgentFactory {
        TestAgent newTestAgent(int id);
    }

    static class ConcurrentAgentFactory implements TestAgentFactory {
        @Override
        public TestAgent newTestAgent(int id) {
            return new ConcurrentAgent();
        }

    }

    enum TestCategory implements TestTaskCategory {
        AliveAgent, SleepingAgent
    }

    static class ConcurrentAgent implements TestAgent {

        @Override
        public TestTask firstTask() {
            return new FirstTask();
        }

        class FirstTask implements TestTask {
            private Thread currentThread;

            @Override
            public TestTaskCategory category() {
                return TestCategory.AliveAgent;
            }

            @Override
            public TestTask nextTaskIfAny() {
                Assert.assertEquals("Running thread doesn't match !",
                        this.currentThread, Thread.currentThread());
                return new SecondTask();
            }

            @Override
            public void run(TestTaskReporter reporter) throws Exception {
                this.currentThread = Thread.currentThread();
                Thread.sleep(200);
                final Time time = reporter.timeSpentSoFar();
                log().debug("Time spent for far: {} ms", time);
            }

            @Override
            public Time when() {
                return TimeSpecificationFactory.someMillisecondsFromNow(1000);
            }
        }

        class SecondTask implements TestTask {
            @Override
            public TestTaskCategory category() {
                return TestCategory.AliveAgent;
            }

            @Override
            public Time when() {
                return TimeSpecificationFactory.someMillisecondsFromNow(1000);
            }

            @Override
            public void run(TestTaskReporter reporter) throws Exception {
                Thread.sleep(100);
            }

            @Override
            public TestTask nextTaskIfAny() {
                return null;
            }

        }
    }

    private static FailTask log(final FailTask failTask) {
        log().info("Got FailTask in our hands...");
        return failTask;
    }

    private static Logger log() {
        return LOG;
    }

    class SleepingMs implements TestAgentFactory {

        private final int sleep;
        private final int schedule;

        public SleepingMs(final int sleep, final int schedule) {
            this.sleep = sleep;
            this.schedule = schedule;
        }

        @Override
        public TestAgent newTestAgent(int id) {
            return new TestAgentWithOneSleepingTask();
        }

        class TestAgentWithOneSleepingTask implements TestAgent {

            @Override
            public TestTask firstTask() {
                return newTestTask(SleepingMs.this.sleep,
                        SleepingMs.this.schedule,
                        newTestTask(SleepingMs.this.sleep, 0, null));
            }

            private TestTask newTestTask(final int sleep, final int schedule,
                    final TestTask next) {
                return new TestTask() {

                    @Override
                    public Time when() {
                        return TimeSpecificationFactory.inMillis(schedule);
                    }

                    @Override
                    public void run(TestTaskReporter reporter) throws Exception {
                        Thread.sleep(sleep);
                    }

                    @Override
                    public TestTask nextTaskIfAny() {
                        return next;
                    }

                    @Override
                    public TestTaskCategory category() {
                        return TestCategory.SleepingAgent;
                    }
                };
            }
        }
    }

    class FailingHalfTheTime implements TestAgent {

        private final TestTask first;

        public FailingHalfTheTime(final TestTask first) {
            this.first = first;
        }

        @Override
        public TestTask firstTask() {
            return this.first;
        }
    }

    class FailTask implements TestTask {

        private long failureSleep;

        public FailTask(final long failureSleep) {
            this.failureSleep = failureSleep;
        }

        @Override
        public Time when() {
            return TimeSpecificationFactory.someMillisecondsFromNow(1500);
        }

        @Override
        public TestTask nextTaskIfAny() {
            return null;
        }

        @Override
        public void run(final TestTaskReporter reporter) throws Exception {
            Thread.sleep(failureSleep());
            throw new Fail("i fail");
        }

        private long failureSleep() {
            return this.failureSleep;
        }

        @Override
        public TestTaskCategory category() {
            return TestCategory.AliveAgent;
        }
    }
}
