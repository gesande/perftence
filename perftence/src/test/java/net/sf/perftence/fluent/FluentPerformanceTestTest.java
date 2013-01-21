package net.sf.perftence.fluent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.sf.perftence.Executable;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.reporting.Duration;

import org.junit.Test;

public class FluentPerformanceTestTest {

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

    private class MyTestFailureNotifier implements TestFailureNotifier {
        @Override
        public void testFailed(Throwable t) {
            FluentPerformanceTestTest.this.testFailed = true;
            FluentPerformanceTestTest.this.testFailure = t;
            assertTrue(t.getClass().equals(MyFailure.class));
        }
    }

    private static class MyFailure {//
    }

}
