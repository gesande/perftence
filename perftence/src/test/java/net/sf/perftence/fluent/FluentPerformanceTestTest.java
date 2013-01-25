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
                new MyTestFailureNotifier());
        MultithreadWorker test = fluentPerformanceTest
                .test("sanityCheck")
                .setup(fluentPerformanceTest.setup().threads(100)
                        .duration(Duration.seconds(5)).build())
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
        final TestBuilder builder = new FluentPerformanceTest(
                new PerfTestFailedNotifier()).test(id());
        builder.setup(setup().threads(1).invocations(2).build())
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
        final TestBuilder builder = new FluentPerformanceTest(
                new PerfTestFailedNotifier()).test(id());
        builder.setup(setup().threads(1).invocations(5).build())
                .requirements(requirements().percentile95(101).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test(expected = PerfTestFailure.class)
    public void percentile95RequirementFails() {
        final TestBuilder builder = new FluentPerformanceTest(
                new PerfTestFailedNotifier()).test(id());
        builder.setup(setup().threads(1).invocations(5).build())
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
        final TestBuilder builder = new FluentPerformanceTest(
                new PerfTestFailedNotifier()).test(id());
        builder.setup(setup().threads(1).invocations(5).build())
                .requirements(requirements().percentile95(100).max(102).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
        assertFalse(this.testFailed);
        assertNull(this.testFailure);
    }

    @Test
    public void allowedException() {
        final AtomicInteger i = new AtomicInteger();
        final TestBuilder builder = new FluentPerformanceTest(
                new PerfTestFailedNotifier()).test(id()).noInvocationGraph();
        builder.setup(setup().threads(1).invocations(5).build())
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

    private class PerfTestFailedNotifier implements TestFailureNotifier {

        @Override
        public void testFailed(final Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(PerfTestFailure.class));
        }

    }

    private class MyTestFailureNotifier implements TestFailureNotifier {
        @Override
        public void testFailed(Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(FailIHave.class));
        }
    }

    private static class FailIHave extends Exception {//
    }

}
