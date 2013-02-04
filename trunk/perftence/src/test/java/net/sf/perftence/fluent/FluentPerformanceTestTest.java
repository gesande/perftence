package net.sf.perftence.fluent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.Executable;
import net.sf.perftence.PerfTestFailure;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.reporting.Duration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class FluentPerformanceTestTest {

    private boolean testFailed;
    private Throwable testFailure;

    @Rule
    public TestName name = new TestName();

    @Test
    public void sanityCheck() {
        final FluentPerformanceTest fluentPerformanceTest = new FluentPerformanceTest(
                new FailIHaveNotifier());
        final MultithreadWorker test = fluentPerformanceTest
                .test(id())
                .setup(fluentPerformanceTest.setup().threads(100)
                        .duration(Duration.seconds(3)).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(10);
                    }
                });
        assertNotNull(
                "Uuh, null returned by fluent based test.test(id).setup(..).executable(..) method!",
                test);
        test.start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test(expected = PerfTestFailure.class)
    public void requirementFailed() {
        final AtomicInteger i = new AtomicInteger();
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id())
                .setup(fluent.setup().threads(1).invocations(2).build())
                .requirements(fluent.requirements().max(200).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(i.intValue() == 1 ? 100 : 201);
                    }
                }).start();
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void percentile95RequirementSucceeds() {
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id())
                .setup(fluent.setup().threads(1).invocations(5).build())
                .requirements(fluent.requirements().percentile95(100).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(99);
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test(expected = PerfTestFailure.class)
    public void percentile95RequirementFails() {
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id())
                .setup(fluent.setup().threads(1).invocations(5).build())
                .requirements(fluent.requirements().percentile95(50).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void percentile95RequirementAndMaxSucceeds() {
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id())
                .setup(fluent.setup().threads(1).invocations(5).build())
                .requirements(
                        fluent.requirements().percentile95(102).max(102)
                                .build()).executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(99);
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test
    public void allowedException() {
        final AtomicInteger i = new AtomicInteger();
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id()).noInvocationGraph()
                .setup(fluent.setup().threads(1).invocations(5).build())
                .allow(FailIHave.class).executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                        if (i.intValue() == 4) {
                            throw new FailIHave();
                        }
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test
    public void allowedExceptionDuringDurationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id())
                .noInvocationGraph()
                .setup(fluent.setup().threads(1).duration(Duration.seconds(5))
                        .build()).allow(FailIHave.class)
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                        if (i.intValue() == 4) {
                            throw new FailIHave();
                        }
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test(expected = RuntimeException.class)
    public void noTestSetup() {
        fluent().test(id()).executable(new Executable() {

            @Override
            public void execute() throws Exception {
                Thread.sleep(100);
            }
        }).start();
    }

    @Test(expected = RuntimeException.class)
    public void noSetup() {
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id()).setup(fluent.setup().noSetup())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
    }

    @Test(expected = PerfTestFailure.class)
    public void invalidTestSetup() {
        final FluentPerformanceTest fluent = fluent();
        fluent.test(id()).setup(fluent.setup().build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
    }

    @Test
    public void failingUnexpectedlyInDurationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        final FluentPerformanceTest fluent = new FluentPerformanceTest(
                new FailIHaveNotifier());
        fluent.test(id())
                .noInvocationGraph()
                .setup(fluent.setup().threads(1).duration(Duration.seconds(5))
                        .build()).executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                        if (i.intValue() == 4) {
                            throw new FailIHave();
                        }
                    }
                }).start();
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void errorOccurredInDurationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        FluentPerformanceTest fluent = new FluentPerformanceTest(
                new ErrorFailureNotifier());
        fluent.test(id())
                .noInvocationGraph()
                .setup(fluent.setup().threads(1).duration(Duration.seconds(5))
                        .build()).executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                        if (i.intValue() == 4) {
                            throw new ErrorOccurred();
                        }
                    }
                }).start();
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void errorOccurredInInvocationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        FluentPerformanceTest fluent = new FluentPerformanceTest(
                new ErrorFailureNotifier());
        fluent.test(id()).noInvocationGraph()
                .setup(fluent.setup().threads(1).invocations(5).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                        if (i.intValue() == 4) {
                            throw new ErrorOccurred();
                        }
                    }
                }).start();
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void invocationsNotSpreadEvenlyBetweenThreads() {
        final AtomicInteger i = new AtomicInteger();
        FluentPerformanceTest fluent = new FluentPerformanceTest(
                new ErrorFailureNotifier());
        fluent.test(id()).noInvocationGraph()
                .setup(fluent.setup().threads(3).invocations(10).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        i.incrementAndGet();
                        Thread.sleep(100);
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @SuppressWarnings({ "unused", "static-method" })
    @Test(expected = TestFailureNotifier.NoTestNotifierException.class)
    public void nullNotifier() {
        new FluentPerformanceTest(null);
    }

    @Test
    public void startable() throws Exception {
        final AtomicBoolean executed = new AtomicBoolean(false);
        final Random random = new Random();
        final FluentPerformanceTest fluent = fluent();
        final MultithreadWorker durationWorker = fluent
                .test(id() + ".1")
                .setup(fluent.setup().threads(10).duration(Duration.seconds(4))
                        .build()).noInvocationGraph()
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        executed.set(true);
                        Thread.sleep(random.nextInt(10) + 1);
                    }
                });
        final TestBuilder startable = fluent.test("root").noInvocationGraph()
                .startable(durationWorker);
        assertFalse(
                "includeInvocationGraph should have been 'false' for MultithreadTestWorkerBuilder!",
                startable.includeInvocationGraph());
        assertFalse(
                "includeInvocationGraph should have been 'false' for MultithreadWorker!",
                durationWorker.includeInvocationGraph());
        startable.start();
        assertTrue(executed.get());

    }

    private FluentPerformanceTest fluent() {
        return new FluentPerformanceTest(new PerfTestFailedNotifier());
    }

    private class PerfTestFailedNotifier implements TestFailureNotifier {

        @Override
        public void testFailed(final Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(PerfTestFailure.class));
        }
    }

    private class FailIHaveNotifier implements TestFailureNotifier {
        @Override
        public void testFailed(final Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(FailIHave.class));
        }
    }

    private static class FailIHave extends Exception {//
    }

    private class ErrorFailureNotifier implements TestFailureNotifier {

        @Override
        public void testFailed(final Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(ErrorOccurred.class));

        }
    }

    private static class ErrorOccurred extends Error {//
    }

    private String id() {
        return testName().getMethodName();
    }

    private TestName testName() {
        return this.name;
    }
}
