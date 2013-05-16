package net.sf.perftence;

import net.sf.perftence.agents.AgentBasedTest;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.fluent.DefaultRunNotifier;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

public final class PerftenceApi {
    private final FluentPerformanceTest performanceTest;
    private final AgentBasedTest agentBasedTest;
    private final TestFailureNotifier notifier;
    private final LatencyProviderFactory latencyProviderFactory;
    private final TestRuntimeReporterFactory testRuntimeReporterFactory;

    public PerftenceApi(final TestFailureNotifier notifier) {
        this.notifier = notifier;
        this.latencyProviderFactory = new DefaultLatencyProviderFactory();
        this.testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory();
        this.performanceTest = createPerformanceTest();
        this.agentBasedTest = createAgentBasedTest();
    }

    private FluentPerformanceTest performanceTest() {
        return this.performanceTest;
    }

    private AgentBasedTest agentBasedTest() {
        return this.agentBasedTest;
    }

    private TestFailureNotifier failureNotifier() {
        return this.notifier;
    }

    private AgentBasedTest createAgentBasedTest() {
        return new AgentBasedTest(failureNotifier(), latencyProviderFactory(),
                testRuntimeReporterFactory());
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private FluentPerformanceTest createPerformanceTest() {
        return new FluentPerformanceTest(failureNotifier(),
                testRuntimeReporterFactory(), new DefaultRunNotifier());
    }

    private TestRuntimeReporterFactory testRuntimeReporterFactory() {
        return this.testRuntimeReporterFactory;
    }

    public net.sf.perftence.agents.TestBuilder agentBasedTest(final String name) {
        return agentBasedTest().test(name);
    }

    public net.sf.perftence.fluent.TestBuilder test(final String name) {
        return performanceTest().test(name);
    }

    public PerformanceRequirementsBuilder requirements() {
        return performanceTest().requirements();
    }

    public PerformanceTestSetupBuilder setup() {
        return performanceTest().setup();
    }

}
