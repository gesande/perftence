package net.sf.perftence;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import net.sf.perftence.reporting.Duration;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Acts as an example of how a developer can define performance test fluently
 * 
 */
@RunWith(DefaultTestRunner.class)
public final class FluentBasedTestExample extends AbstractMultiThreadedTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(FluentBasedTestExample.class);
    private Executable executor;
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private int i;

    private final Executable executor() {
        return this.executor;
    }

    @Before
    public void before() throws Exception {
        this.i = 0;
        this.executor = new Executable() {
            @Override
            public void execute() {
                FluentBasedTestExample.this.i++;
                if (FluentBasedTestExample.this.i % 1000 == 0) {
                    log().info("Executing...{}",
                            FluentBasedTestExample.this.i);
                }
            }
        };
    }

    @SuppressWarnings("static-method")
    @After
    public void after() {
        log().info("Its over...finally...phuuiiii, i'm done.");
    }

    private static Logger log() {
        return LOG;
    }

    @SuppressWarnings("static-method")
    @Test
    public void learning() {
        final AtomicInteger i = new AtomicInteger(0);
        for (int index = 0; index < 100; index++) {
            log().info("{} % 10 == 0 -> {}", i.intValue(),
                    i.intValue() % 10 == 0);
            i.incrementAndGet();
        }

    }

    @Ignore
    @Test
    public void failedTest() {
        final AtomicInteger i = new AtomicInteger(0);
        test().setup(setup().threads(3).invocations(100).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        boolean b = i.intValue() % 90 == 0;
                        log().info("b = {}", b);
                        if (b) {
                            throw new Exception("Just failing for fun");
                        }
                    }
                }).start();
    }

    @Test
    public void invocationCount() throws Exception {
        final AtomicInteger i = new AtomicInteger(0);
        test().setup(setup().threads(3).invocations(10).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        executor().execute();
                        i.incrementAndGet();
                    }
                }).start();
        Assert.assertEquals("Invocation count doesn't match!", 10, i.get());
    }

    @Test
    public void invocationBased() throws Exception {
        test().setup(setup().invocations(100).invocationRange(10).build()).

        executable(new Executable() {
            @Override
            public void execute() throws Exception {
                executor().execute();
            }
        }).start();
    }

    @Test
    public void threadBased() throws Exception {
        test().setup(
                setup().threads(100).invocations(5000).invocationRange(20)
                        .build()).executable(new Executable() {
            @Override
            public void execute() throws Exception {
                executor().execute();
            }
        }).start();
    }

    @Test
    public void durationBasedSingleThread() throws Exception {
        test().setup(setup().duration(Duration.seconds(5)).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
    }

    @Test
    public void durationBasedMultiThread() throws Exception {
        test().setup(
                setup().duration(Duration.seconds(2)).threads(2)
                        .invocationRange(20).throughputRange(2000000).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        executor().execute();
                    }
                }).start();
    }

    @Test
    public void performanceRequirements() throws Exception {
        test().setup(setup().threads(10).invocations(100).build())
                .requirements(
                        requirements().average(30).max(200).median(20).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        executor().execute();
                    }
                }).start();
    }

    @Test
    public void durationMultiThreadSleepingExecutable() throws Exception {
        test().setup(
                setup().duration(Duration.seconds(5)).threads(10)
                        .invocationRange(100).throughputRange(500).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(RANDOM.nextInt(100) + 1);
                    }
                }).start();
    }

    @Test
    public void durationMultiThreadSleepingMoreThanOneSecondExecutable()
            throws Exception {
        test().setup(
                setup().duration(Duration.seconds(15)).threads(10)
                        .invocationRange(1000).throughputRange(30).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(RANDOM.nextInt(1000));
                    }
                }).start();
    }

    @Test
    public void customSummaryAppender() throws Exception {
        test().setup(
                setup().duration(Duration.seconds(15)).threads(10)
                        .invocationRange(1000).throughputRange(30)
                        .summaryAppender(new SummaryAppender() {
                            @Override
                            public void append(Summary<?> summary) {
                                summary.text("Here's something cool!")
                                        .endOfLine()
                                        .bold("And some bolded text")
                                        .endOfLine();
                            }
                        }).build()).executable(new Executable() {

            @Override
            public void execute() throws Exception {
                Thread.sleep(RANDOM.nextInt(1000));
            }
        }).start();
    }

    @Test
    public void noInvocationGraph() throws Exception {
        test().noInvocationGraph()
                .setup(setup().duration(Duration.seconds(5)).threads(10)
                        .invocationRange(1000).throughputRange(30).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(RANDOM.nextInt(1000));
                    }
                }).start();
    }

    @Ignore
    @Test
    public void durationBasedTestWithWaitingMoreThanDuration() throws Exception {
        test().setup(
                setup().threads(100).duration(Duration.seconds(10)).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        int wait = RANDOM.nextInt(100) + 1;
                        log().debug("Wait time {}", wait);
                        Thread.sleep(Duration.seconds(wait));
                    }
                }).start();
    }

    @Test
    public void threadBasedWithAllowedException() throws Exception {
        final ExecutorBehavingBadlyHalfTheTime failingExecutor = new ExecutorBehavingBadlyHalfTheTime();

        test().setup(
                setup().threads(10).invocations(100).throughputRange(50)
                        .build()).allow(MyException.class)
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        failingExecutor.execute();
                    }
                }).start();
    }

    class ExecutorBehavingBadlyHalfTheTime implements Executable {
        final AtomicInteger counter = new AtomicInteger();

        @Override
        public void execute() throws Exception {
            if (this.counter.get() % 2 == 0) {
                this.counter.incrementAndGet();
                throw new MyException(
                        "null pointer executing NullPointerExceptionExecutor");
            }
            this.counter.incrementAndGet();
            Thread.sleep(500);
        }

    }

    class MyException extends Exception {

        public MyException(final String msg) {
            super(msg);
        }
    }
}
