package net.sf.perftence;

import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.agents.AgentBasedTest;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;

import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class AbstractMultiThreadedTest {

    private static TestFailureNotifier failureNotifier;
    private final FluentPerformanceTest performanceTest;
    private final AgentBasedTest agentBasedTest;

    public AbstractMultiThreadedTest() {
        this.performanceTest = createPerformanceTest();
        this.agentBasedTest = createAgentBasedTest();
    }

    private AgentBasedTest createAgentBasedTest() {
        return new AgentBasedTest(failureNotifier());
    }

    private FluentPerformanceTest createPerformanceTest() {
        return new FluentPerformanceTest(failureNotifier());
    }

    /**
     * Rule used for tracking the test name
     */
    @Rule
    public TestName name = new TestName();

    private FluentPerformanceTest performanceTest() {
        return this.performanceTest;
    }

    private AgentBasedTest newAgentBasedTest() {
        return this.agentBasedTest;
    }

    /**
     * Uses fully qualified method name (with class name) as the name of the
     * agent test
     */
    protected final net.sf.perftence.agents.TestBuilder agentBasedTest() {
        return agentBasedTest(id());
    }

    protected final net.sf.perftence.agents.TestBuilder agentBasedTest(
            final String name) {
        return newAgentBasedTest().test(name);
    }

    @SuppressWarnings("static-method")
    protected final TestFailureNotifier failureNotifier() {
        return failureNotifier;
    }

    /**
     * This gets called by the AbstractTestRunner using reflection
     */
    public static void failureNotifier(final TestFailureNotifier notifier) {
        AbstractMultiThreadedTest.failureNotifier = notifier;
    }

    protected final net.sf.perftence.fluent.TestBuilder test() {
        return test(id());
    }

    protected final net.sf.perftence.fluent.TestBuilder test(String id) {
        return performanceTest().test(id);
    }

    protected final String id() {
        return fullyQualifiedMethodNameWithClassName();
    }

    protected final String fullyQualifiedMethodNameWithClassName() {
        return new StringBuffer(this.getClass().getName()).append(".")
                .append(testMethodName()).toString();
    }

    protected final String testMethodName() {
        return testName().getMethodName();
    }

    private TestName testName() {
        return this.name;
    }

    protected PerformanceRequirementsBuilder requirements() {
        return performanceTest().requirements();
    }

    protected PerformanceTestSetupBuilder setup() {
        return performanceTest().setup();
    }
}
