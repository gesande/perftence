package net.sf.perftence.experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.agents.ActiveThreads;
import net.sf.perftence.agents.StorageForThreadsRunningCurrentTasks;
import net.sf.perftence.agents.TestAgent;
import net.sf.perftence.agents.TestTask;
import net.sf.perftence.agents.TestTaskCategory;
import net.sf.perftence.agents.TestTaskReporter;
import net.sf.perftence.agents.Time;
import net.sf.perftence.agents.TimeSpecificationFactory;
import net.sf.perftence.reporting.DefaultDoubleFormatter;
import net.sf.perftence.reporting.FailedInvocations;
import net.sf.perftence.reporting.FailedInvocationsFactory;
import net.sf.perftence.reporting.InvocationReporter;
import net.sf.perftence.reporting.InvocationReporterFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.FieldFormatter;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class DirectThreadModelTests extends AbstractMultiThreadedTest {

    private final static Logger LOG = LoggerFactory
            .getLogger(DirectThreadModelTests.class);
    private LatencyProvider latencyProvider;
    private AtomicInteger tasksRun;
    private AtomicInteger tasksFailed;
    private InvocationReporter newDefaultInvocationReporter;
    private StorageForThreadsRunningCurrentTasks newStorage;
    private ActiveThreads activeThreads;
    private LatencyFactory latencyFactory;

    enum SleepingTestCategory implements TestTaskCategory {
        SleepingAgent, AliveAgent, CounterAgent, DoubleAgent
    }

    @Ignore
    @Test
    public void sleepingAgentStoryWithOneTaskWithDirectThreadModel()
            throws InterruptedException {
        final int userCount = 10000;
        this.latencyFactory = new LatencyFactory();
        this.latencyProvider = new LatencyProvider();
        this.tasksRun = new AtomicInteger();
        this.tasksFailed = new AtomicInteger();
        this.newStorage = StorageForThreadsRunningCurrentTasks.newStorage(id());
        PerformanceTestSetupBuilder setup = setup().threads(userCount);
        setup.graphWriter(this.newStorage.graphWriter());
        setup.summaryAppender(this.newStorage.summaryAppender());
        this.newDefaultInvocationReporter = InvocationReporterFactory
                .newDefaultInvocationReporter(this.latencyProvider, true,
                        setup.build(), newFailedInvocations());
        final SleepingTestAgentFactoryWithNowFlavour agentFactory = new SleepingTestAgentFactoryWithNowFlavour();
        this.activeThreads = new ActiveThreads();
        this.latencyProvider.start();
        final List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < userCount; i++) {
            threads.add(new Thread(new AnotherRunner(agentFactory
                    .newTestAgent(userCount)), "onetask-thread-" + i));
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).join();
        }
        this.latencyProvider.stop();
        this.newDefaultInvocationReporter.summary(id(),
                this.latencyProvider.duration(),
                this.latencyProvider.sampleCount(),
                this.latencyProvider.startTime());
    }

    private static FailedInvocations newFailedInvocations() {
        return new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                new AdjustedFieldBuilderFactory(new FieldFormatter(),
                        new FieldAdjuster()).newInstance()).newInstance();
    }

    @Ignore
    @Test
    public void sleepingAgentStoryWithTwoTasksWithDirectThreadModel()
            throws InterruptedException {
        final int userCount = 10000;
        this.latencyFactory = new LatencyFactory();
        this.latencyProvider = new LatencyProvider();
        this.tasksRun = new AtomicInteger();
        this.tasksFailed = new AtomicInteger();
        this.newStorage = StorageForThreadsRunningCurrentTasks.newStorage(id());
        PerformanceTestSetupBuilder setup = setup().threads(userCount);
        setup.graphWriter(this.newStorage.graphWriter());
        setup.summaryAppender(this.newStorage.summaryAppender());

        this.newDefaultInvocationReporter = InvocationReporterFactory
                .newDefaultInvocationReporter(this.latencyProvider, true,
                        setup.build(), newFailedInvocations());
        this.activeThreads = new ActiveThreads();
        this.latencyProvider.start();
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < userCount; i++) {
            threads.add(new Thread(new AnotherRunner(
                    new SleepingTestAgentFactoryWithNowFlavourHavingNextTask()
                            .newTestAgent(userCount)), "thread-" + i));
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).join();
        }
        this.latencyProvider.stop();
        this.newDefaultInvocationReporter.summary(id(),
                this.latencyProvider.duration(),
                this.latencyProvider.sampleCount(),
                this.latencyProvider.startTime());
    }

    interface TestAgentFactory {
        TestAgent newTestAgent(int id);
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

    class AnotherRunner implements Runnable {

        private TestTask task;

        public AnotherRunner(final TestAgent agent) {
            this.task = agent.firstTask();
        }

        @Override
        public void run() {
            DirectThreadModelTests.this.newStorage.store(
                    DirectThreadModelTests.this.latencyProvider
                            .currentDuration(),
                    DirectThreadModelTests.this.activeThreads.more());
            while (this.task != null) {
                final long t1 = System.nanoTime();
                try {
                    LOG.debug("Running task");
                    this.task.run(null);
                } catch (Throwable e) {
                    LOG.error("Error running task", e);
                    DirectThreadModelTests.this.tasksFailed.incrementAndGet();
                } finally {
                    LOG.debug("Task done. (Tasks run: {})",
                            DirectThreadModelTests.this.tasksRun
                                    .incrementAndGet());
                    final int newLatency = DirectThreadModelTests.this.latencyFactory
                            .newLatency(t1);
                    DirectThreadModelTests.this.latencyProvider
                            .addSample(newLatency);
                    DirectThreadModelTests.this.newDefaultInvocationReporter
                            .latency(newLatency);
                    this.task = this.task.nextTaskIfAny();
                }
            }
            DirectThreadModelTests.this.newStorage.store(
                    DirectThreadModelTests.this.latencyProvider
                            .currentDuration(),
                    DirectThreadModelTests.this.activeThreads.less());
        }
    }

}
