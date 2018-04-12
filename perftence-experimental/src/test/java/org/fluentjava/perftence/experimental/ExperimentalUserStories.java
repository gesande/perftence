package org.fluentjava.perftence.experimental;

import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.bag.SynchronizedBag;
import org.fluentjava.perftence.AbstractMultiThreadedTest;
import org.fluentjava.perftence.DefaultLatencyProviderFactory;
import org.fluentjava.perftence.DefaultTestRunner;
import org.fluentjava.perftence.Executable;
import org.fluentjava.perftence.LatencyFactory;
import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.agents.TestAgent;
import org.fluentjava.perftence.agents.TestTask;
import org.fluentjava.perftence.agents.TestTaskCategory;
import org.fluentjava.perftence.agents.TestTaskReporter;
import org.fluentjava.perftence.agents.Time;
import org.fluentjava.perftence.agents.TimeSpecificationFactory;
import org.fluentjava.perftence.common.FrequencyStorage;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.DatasetAdapter;
import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.ImageFactoryUsingJFreeChart;
import org.fluentjava.perftence.graph.jfreechart.JFreeChartWriter;
import org.fluentjava.perftence.graph.jfreechart.LineChartGraphData;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocations;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class ExperimentalUserStories extends AbstractMultiThreadedTest {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private LatencyProvider latencyProvider;
    private TestRuntimeReporter testRuntimeReporter;
    private AtomicInteger tasksRun;
    private AtomicInteger tasksFailed;
    private Bag latencyFreqs;
    private LatencyFactory latencyFactory;

    enum SleepingTestCategory implements TestTaskCategory {
        SleepingAgent, AliveAgent, CounterAgent, DoubleAgent
    }

    @Test
    public void printingAFrequencyGraphUsingPerfEngineTools() throws InterruptedException {
        this.latencyFactory = new LatencyFactory();
        this.latencyFreqs = SynchronizedBag.decorate(new HashBag());
        final FrequencyStorage storage = newFrequencyStorage(this.latencyFreqs);
        this.tasksRun = new AtomicInteger();
        this.tasksFailed = new AtomicInteger();
        final int userCount = 16000;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            threads.add(new Thread(new Runner(new TestAgentWithTwoTasks())));
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).join();
        }
        System.out.println("\n" + this.latencyFreqs);
        System.out.println("\ntasks run " + this.tasksRun.intValue());
        System.out.println("\ntasks failed " + this.tasksFailed.intValue());

        new ImageFactoryUsingJFreeChart(
                new JFreeChartWriter(HtmlTestReport.withDefaultReportPath().reportRootDirectory()))
                        .createXYLineChart("printingAFrequencyGraphUsingPerfEngineTools", storage.imageData());
    }

    @Test
    public void printAllTheGraphsAndHtmlSummary() throws InterruptedException {
        final int userCount = 16000;
        this.latencyFactory = new LatencyFactory();
        this.latencyProvider = newLatencyProvider();
        this.tasksRun = new AtomicInteger();
        this.tasksFailed = new AtomicInteger();
        this.testRuntimeReporter = TestRuntimeReporterFactoryUsingJFreeChart
                .reporterFactory(HtmlTestReport.withDefaultReportPath()).newRuntimeReporter(this.latencyProvider, true,
                        setup().threads(userCount).build(), newFailedInvocations());
        this.latencyProvider.start();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            threads.add(new Thread(new AnotherRunner(new TestAgentWithTwoTasks())));
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < userCount; i++) {
            threads.get(i).join();
        }
        this.latencyProvider.stop();
        System.out.println(this.latencyProvider.toString());
        this.testRuntimeReporter.summary(id(), this.latencyProvider.duration(), this.latencyProvider.sampleCount(),
                this.latencyProvider.startTime());

    }

    private static LatencyProvider newLatencyProvider() {
        return new DefaultLatencyProviderFactory().newInstance();
    }

    @Test
    public void runWithPerfTest() {
        this.latencyFactory = new LatencyFactory();
        this.tasksRun = new AtomicInteger();
        this.tasksFailed = new AtomicInteger();
        test().setup(setup().threads(16000).invocations(16000).build()).executable(new Executable() {
            @Override
            public void execute() throws Exception {
                new ThirdRunner(new TestAgentWithTwoTasks()).run();
            }
        }).start();
    }

    class ThirdRunner implements Runnable {
        private TestTask task;

        public ThirdRunner(final TestAgent agent) {
            this.task = agent.firstTask();
        }

        @Override
        public void run() {
            while (this.task != null) {
                try {
                    this.task.run(null);
                } catch (Throwable e) {
                    System.err.println(e);
                    ExperimentalUserStories.this.tasksFailed.incrementAndGet();
                } finally {
                    System.out.print("(" + ExperimentalUserStories.this.tasksRun.incrementAndGet() + ")");
                    this.task = this.task.nextTaskIfAny();
                }
            }
        }

    }

    class AnotherRunner implements Runnable {

        private TestTask task;

        public AnotherRunner(final TestAgent agent) {
            this.task = agent.firstTask();
        }

        @Override
        public void run() {
            while (this.task != null) {
                long t1 = System.nanoTime();
                try {
                    this.task.run(null);
                } catch (Throwable e) {
                    System.err.println(e);
                    ExperimentalUserStories.this.tasksFailed.incrementAndGet();
                } finally {
                    System.out.println("(" + ExperimentalUserStories.this.tasksRun.incrementAndGet() + ")");
                    final int newLatency = ExperimentalUserStories.this.latencyFactory.newLatency(t1);
                    ExperimentalUserStories.this.latencyProvider.addSample(newLatency);
                    ExperimentalUserStories.this.testRuntimeReporter.latency(newLatency);
                    this.task = this.task.nextTaskIfAny();
                }
            }
        }
    }

    final static class TestAgentWithTwoTasks implements TestAgent {
        @Override
        public TestTask firstTask() {
            return newTask(0, RANDOM.nextInt(100) + 1, newTask(0, RANDOM.nextInt(100) + 1, null));
        }
    }

    private static TestTask newTask(final int scheduled, final int sleep, final TestTask next) {
        return new TestTask() {

            @Override
            public Time when() {
                return TimeSpecificationFactory.someMillisecondsFromNow(scheduled);
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

    class Runner implements Runnable {

        private TestTask task;

        public Runner(final TestAgent agent) {
            this.task = agent.firstTask();
        }

        @Override
        public void run() {
            while (this.task != null) {
                long t1 = System.nanoTime();
                try {
                    this.task.run(null);
                } catch (Throwable e) {
                    System.err.println(e);
                    ExperimentalUserStories.this.tasksFailed.incrementAndGet();
                } finally {
                    // long t2 = System.nanoTime();
                    System.out.print("\n(" + ExperimentalUserStories.this.tasksRun.incrementAndGet() + ")");
                    // double latency = t2 - t1;
                    // double asSeconds = latency / 1000 / 1000 / 1000;
                    // String asString = String.format(Locale.ENGLISH, "%.1f",
                    // asSeconds);
                    ExperimentalUserStories.this.latencyFreqs
                            .add(ExperimentalUserStories.this.latencyFactory.newLatency(t1));// asSeconds);
                    this.task = this.task.nextTaskIfAny();
                }
            }
        }

    }

    private static FailedInvocations newFailedInvocations() {
        final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory = new AdjustedFieldBuilderFactory(
                new FieldFormatter(), new FieldAdjuster());
        return new FailedInvocationsFactory(new DefaultDoubleFormatter(), adjustedFieldBuilderFactory.newInstance())
                .newInstance();
    }

    private static FrequencyStorage newFrequencyStorage(final Bag values) {
        return new FrequencyStorage() {
            @Override
            public ImageData imageData() {
                final String legendTitle = legendTitle();
                final ImageData imageData = newImageData(legendTitle);
                long range = 0;
                for (final Object value : values.uniqueSet()) {
                    final int count = values.getCount(value);
                    if (count > range) {
                        range = count;
                    }
                    imageData.add((Number) value, count);
                }
                return imageData.range(range);
            }

            @Override
            public boolean hasSamples() {
                return !values.isEmpty();
            }
        };
    }

    private static String legendTitle() {
        return "Frequency";
    }

    private static ImageData newImageData(final String legendTitle) {
        final ImageData imageData = ImageData.noStatistics("Latency frequencies", "Latency (ms)",
                adapterForLinechart(legendTitle));
        return imageData;
    }

    private static DatasetAdapter<LineChartGraphData, Paint> adapterForLinechart(final String legendTitle) {
        return new DefaultDatasetAdapterFactory().forLineChart(legendTitle);
    }

}
