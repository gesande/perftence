package net.sf.perftence.distributed;

import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.TestBuilder;
import net.sf.perftence.setup.PerformanceTestSetup;

public final class DistributedPerformanceTest {
    private final String id;
    private final TestFailureNotifier testFailureNotifier;
    private final TestRuntimeReporterFactory testRuntimeReporterFactory;
    private final RunNotifier runNotifier;

    public DistributedPerformanceTest(final String id,
            final TestFailureNotifier testFailureNotifier,
            final TestRuntimeReporterFactory testRuntimeReporterFactory,
            final RunNotifier runNotifier) {
        this.id = id;
        this.testFailureNotifier = testFailureNotifier;
        this.testRuntimeReporterFactory = testRuntimeReporterFactory;
        this.runNotifier = runNotifier;
    }

    public TestBuilder setup(final PerformanceTestSetup setup) {
        return new FluentPerformanceTest(this.testFailureNotifier,
                this.testRuntimeReporterFactory, this.runNotifier).test(id())
                .setup(setup);
    }

    private String id() {
        return this.id;
    }
}