package net.sf.perftence.experimental;

import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.agents.AgentBasedTest;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;

public final class PerftenceApi implements TestFailureNotifier {
    private final FluentPerformanceTest performanceTest;
    private final AgentBasedTest agentBasedTest;
    private final TestFailureNotifier notifier;

    public PerftenceApi(final TestFailureNotifier notifier) {
        this.notifier = notifier;
        this.performanceTest = createPerformanceTest();
        this.agentBasedTest = createAgentBasedTest();
    }

    private FluentPerformanceTest performanceTest() {
        return this.performanceTest;
    }

    private AgentBasedTest newAgentBasedTest() {
        return this.agentBasedTest;
    }

    private TestFailureNotifier failureNotifier() {
        return this.notifier;
    }

    private AgentBasedTest createAgentBasedTest() {
        return new AgentBasedTest(failureNotifier());
    }

    private FluentPerformanceTest createPerformanceTest() {
        return new FluentPerformanceTest(failureNotifier());
    }

    @Override
    public void testFailed(final Throwable t) {
        failureNotifier().testFailed(t);
    }

    public net.sf.perftence.agents.TestBuilder agentBasedTest(final String name) {
        return newAgentBasedTest().test(name);
    }

    public net.sf.perftence.fluent.TestBuilder test(final String name) {
        return performanceTest().test(name);
    }

    protected PerformanceRequirementsBuilder requirements() {
        return performanceTest().requirements();
    }

    protected PerformanceTestSetupBuilder setup() {
        return performanceTest().setup();
    }

}
