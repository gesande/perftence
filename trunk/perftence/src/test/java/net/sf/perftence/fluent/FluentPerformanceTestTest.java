package net.sf.perftence.fluent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;
import net.sf.perftence.PerfTestFailure;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.reporting.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class FluentPerformanceTestTest extends AbstractMultiThreadedTest {

    private boolean testFailed;
    private Throwable testFailure;

    @Test
    public void sanityCheck() {
        FluentPerformanceTest fluentPerformanceTest = new FluentPerformanceTest(
                new FailIHaveNotifier());
        MultithreadWorker test = fluentPerformanceTest
                .test("sanityCheck")
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
        when().setup(setup().threads(1).invocations(2).build())
                .requirements(requirements().max(200).build())
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
        when().setup(setup().threads(1).invocations(5).build())
                .requirements(requirements().percentile95(100).build())
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
        when().setup(setup().threads(1).invocations(5).build())
                .requirements(requirements().percentile95(50).build())
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
        when().setup(setup().threads(1).invocations(5).build())
                .requirements(requirements().percentile95(102).max(102).build())
                .executable(new Executable() {
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
        when().noInvocationGraph()
                .setup(setup().threads(1).invocations(5).build())
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
        when().noInvocationGraph()
                .setup(setup().threads(1).duration(Duration.seconds(5)).build())
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

    @Test(expected = RuntimeException.class)
    public void noTestSetup() {
        when().executable(new Executable() {

            @Override
            public void execute() throws Exception {
                Thread.sleep(100);
            }
        }).start();
    }

    @Test(expected = RuntimeException.class)
    public void noSetup() {
        when().setup(setup().noSetup()).executable(new Executable() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(100);
            }
        }).start();
    }

    @Test(expected = PerfTestFailure.class)
    public void invalidTestSetup() {
        when().setup(setup().build()).executable(new Executable() {

            @Override
            public void execute() throws Exception {
                Thread.sleep(100);
            }
        }).start();
    }

    @Test
    public void failingUnexpectedlyInDurationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        new FluentPerformanceTest(new FailIHaveNotifier())
                .test(id())
                .noInvocationGraph()
                .setup(setup().threads(1).duration(Duration.seconds(5)).build())
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
        assertTrue(this.testFailed);
        assertNotNull(this.testFailure);
    }

    @Test
    public void errorOccurredInDurationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        new FluentPerformanceTest(new ErrorFailureNotifier())
                .test(id())
                .noInvocationGraph()
                .setup(setup().threads(1).duration(Duration.seconds(5)).build())
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
    public void errorOccurredInInvocationBasedTest() {
        final AtomicInteger i = new AtomicInteger();
        new FluentPerformanceTest(new ErrorFailureNotifier()).test(id())
                .noInvocationGraph()
                .setup(setup().threads(1).invocations(5).build())
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
        new FluentPerformanceTest(new ErrorFailureNotifier()).test(id())
                .noInvocationGraph()
                .setup(setup().threads(3).invocations(10).build())
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

    private TestBuilder when() {
        return new FluentPerformanceTest(new PerfTestFailedNotifier())
                .test(id());
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

}
