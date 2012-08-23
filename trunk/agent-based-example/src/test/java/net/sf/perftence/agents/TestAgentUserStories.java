package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class TestAgentUserStories extends AbstractMultiThreadedTest {

    private final static Random RANDOM = new Random(System.currentTimeMillis());

    @Test
    public void sleepingAgentStoryWith10Agents() {
        agentBasedTest().agents(agents(10, new SleepingTestAgentFactory()))
                .start();
    }

    @Test
    public void sleepingAgentStoryWith10AgentsWithCustomSummaryAppender() {
        SummaryAppender appender = newSummaryAppender();
        agentBasedTest().agents(agents(10, new SleepingTestAgentFactory()))
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
    public void sleepingAgentWithFixedAmountOfWorkers() {
        agentBasedTest().workers(1000)
                .agents(agents(1000, new SleepingTestAgentFactory()))
                .latencyGraphForAll().start();
    }

    class Fail extends Exception {

        public Fail(final String message) {
            super(message);
        }
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
            return 10 + this.id;
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
            return TimeSpecificationFactory.someMillisecondsFromNow(100);
        }

        @Override
        public TestTask nextTaskIfAny() {
            return hasNextTask() ? newSleepingTask(sleepValue(), false) : null;
        }

        private int sleepValue() {
            return 50 + sleep();
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

}
