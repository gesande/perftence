package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(DefaultTestRunner.class)
public class TestAgentUserStories extends AbstractMultiThreadedTest {

    private final static Random RANDOM = new Random(System.currentTimeMillis());
    private final static Logger LOG = LoggerFactory
            .getLogger(TestAgentUserStories.class);

    @Test
    public void sleepingAgentStoryWith100Agents() {
        agentBasedTest().agents(agents(100, new SleepingTestAgentFactory()))
                .start();
    }

    @Test
    public void sleepingAgentStoryWith100AgentsWithCustomSummaryAppender() {
        SummaryAppender appender = newSummaryAppender();
        agentBasedTest().agents(agents(100, new SleepingTestAgentFactory()))
                .summaryAppender(appender).start();
    }

    private static SummaryAppender newSummaryAppender() {
        return new SummaryAppender() {

            @Override
            public void append(Summary<?> summary) {
                summary.bold("Summary caption").endOfLine();
                summary.text("Some test summary specific text").endOfLine();
                summary.endOfLine();
            }
        };
    }

    @Test
    public void sleepingAgentStory() {
        agentBasedTest().agents(agents(1500, new SleepingTestAgentFactory()))
                .latencyGraphForAll().start();
    }

    @Test
    public void sleepingAgentStoryWithSpecifiedLatencyGraph() {
        agentBasedTest("sleepingAgentStoryWithSpecifiedLatencyGraph")
                .agents(agents(10, new SleepingTestAgentFactory()))
                .latencyGraphFor(SleepingTestCategory.AliveAgent).start();
    }

    @Test
    public void concurrentStoryWithSpecifiedLatencyGraph() {
        agentBasedTest().agents(agents(2, new ConcurrentAgentFactory()))
                .latencyGraphFor(SleepingTestCategory.AliveAgent).start();
    }

    @Test
    public void sleepingAgentWithFixedAmountOfWorkers() {
        agentBasedTest().workers(1000)
                .agents(agents(1000, new SleepingTestAgentFactory()))
                .latencyGraphForAll().start();
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
    public void oneAgentOneTask() {
        agentBasedTest().workerWaitTime(50).invocationRange(100)
                .throughputRange(6500)
                .agents(agents(100000, new SleepingMs(40, 50))).workers(1000)
                .start();
    }

    @Test
    public void agentBasedTestWithAllowedExceptions() {
        agentBasedTest().agents(failingAgents(100)).allow(Fail.class).start();
    }

    class Fail extends Exception {

        public Fail(final String message) {
            super(message);
        }
    }

    private Collection<TestAgent> failingAgents(int agents) {
        final List<TestAgent> list = new ArrayList<TestAgent>();
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < agents; i++) {
            TestTask first = (counter.get() % 2 == 0) ? new FailTask()
                    : newTask(10, 100, null);
            counter.incrementAndGet();
            list.add(new FailingHalfTheTime(first));
        }
        return list;
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

        @Override
        public Time when() {
            return TimeSpecificationFactory.now();
        }

        @Override
        public TestTask nextTaskIfAny() {
            return null;
        }

        @Override
        public void run(TestTaskReporter reporter) throws Exception {
            Thread.sleep(1000);
            throw new Fail("i fail");
        }

        @Override
        public TestTaskCategory category() {
            return SleepingTestCategory.AliveAgent;
        }

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
                        return SleepingTestCategory.SleepingAgent;
                    }
                };
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
                return SleepingTestCategory.AliveAgent;
            }
        };
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

    static class ConcurrentAgent implements TestAgent {

        @Override
        public TestTask firstTask() {
            return new FirstTask();
        }

        class FirstTask implements TestTask {
            private Thread currentThread;

            @Override
            public TestTaskCategory category() {
                return SleepingTestCategory.AliveAgent;
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
                return SleepingTestCategory.AliveAgent;
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

    class SleepingTestAgentFactory implements TestAgentFactory {

        @Override
        public TestAgent newTestAgent(final int id) {
            return new SleepingTestAgent(id);
        }
    }

    class SleepingTestAgent implements TestAgent {
        private final int id;
        private TestTask firstTask;

        public SleepingTestAgent(final int id) {
            this.id = id;
            this.firstTask = newSleepingTask(sleepValue(),
                    evaluateIfNextTaskIsNeeded());
        }

        @Override
        public TestTask firstTask() {
            return this.firstTask;
        }

        private int sleepValue() {
            return 100 + this.id;
        }

    }

    enum SleepingTestCategory implements TestTaskCategory {
        SleepingAgent, AliveAgent, CounterAgent, DoubleAgent
    }

    class SleepingTask implements TestTask {

        private final int sleep;
        private final boolean nextTask;
        private final SleepingTestCategory category;

        @Override
        public TestTaskCategory category() {
            return this.category;
        }

        @Override
        public void run(final TestTaskReporter reporter) {
            try {
                Thread.sleep(sleep());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private int sleep() {
            return this.sleep;
        }

        @Override
        public String toString() {
            return "SleepingTask [sleep=" + sleep() + ", category = "
                    + category() + ", nextTask=" + hasNextTask() + "]";
        }

        private boolean hasNextTask() {
            return this.nextTask;
        }

        @Override
        public Time when() {
            return TimeSpecificationFactory.someMillisecondsFromNow(500);
        }

        @Override
        public TestTask nextTaskIfAny() {
            return hasNextTask() ? newSleepingTask(sleepValue(), false) : null;
        }

        private int sleepValue() {
            return 600 + sleep();
        }

        private SleepingTask(final int sleep, final boolean nextTask,
                final SleepingTestCategory category) {
            this.sleep = sleep;
            this.nextTask = nextTask;
            this.category = category;
        }

    }

    private static boolean evaluateIfNextTaskIsNeeded() {
        return (nextInt(100) + 1) < 80;
    }

    private static SleepingTestCategory randomCategory() {
        return randomCategory(SleepingTestCategory.values());
    }

    private static SleepingTestCategory randomCategory(
            final SleepingTestCategory[] values) {
        return values[nextInt(values.length)];
    }

    private TestTask newSleepingTask(final int sleep, final boolean nextTask) {
        return new SleepingTask(nextInt(sleep) + 1, nextTask, randomCategory());
    }

    private static int nextInt(final int value) {
        return RANDOM.nextInt(value);
    }

    private static Logger log() {
        return LOG;
    }

}
