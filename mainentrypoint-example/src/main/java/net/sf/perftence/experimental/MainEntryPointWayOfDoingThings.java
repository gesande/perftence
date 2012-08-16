package net.sf.perftence.experimental;

import java.util.Random;

import net.sf.perftence.Executable;
import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.MultithreadWorker;
import net.sf.perftence.fluent.TestBuilder;
import net.sf.perftence.reporting.Duration;

public final class MainEntryPointWayOfDoingThings implements
        TestFailureNotifier {

    private FluentPerformanceTest performanceTest;
    private final static Random RANDOM = new Random(System.currentTimeMillis());

    public MainEntryPointWayOfDoingThings() {
        this.performanceTest = new FluentPerformanceTest(this);
    }

    public static void main(final String[] args) throws Exception {
        MainEntryPointWayOfDoingThings instance = new MainEntryPointWayOfDoingThings();
        instance.doThings();
    }

    private void doThings() throws Exception {
        final int duration = Duration.seconds(10);
        test1(duration).start();
        test2(duration).start();

        performanceTest().test("multiple-tests-concurrently")
                .startable(test1(duration)).startable(test2(duration)).start();
    }

    private MultithreadWorker test2(final int duration) throws Exception {
        return test("test2").setup(
                setup().duration(duration).threads(5).build()).executable(
                new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(RANDOM.nextInt(50) + 1);
                    }
                });
    }

    private MultithreadWorker test1(final int duration) throws Exception {
        return test("test1").setup(
                setup().duration(duration).threads(10).build()).executable(
                new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(RANDOM.nextInt(100) + 1);
                    }
                });
    }

    private TestBuilder test(final String id) {
        return performanceTest().test(id);
    }

    private PerformanceTestSetupBuilder setup() {
        return performanceTest().setup();
    }

    FluentPerformanceTest performanceTest() {
        return this.performanceTest;
    }

    @Override
    public void testFailed(final Throwable t) {
        // no implementation
    }
}
