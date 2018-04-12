package org.fluentjava.perftence;

import org.fluentjava.perftence.api.DefaultPerftenceApiFactory;
import org.fluentjava.perftence.api.PerftenceApi;
import org.fluentjava.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class AbstractMultiThreadedTest {

    private static TestFailureNotifier failureNotifier;
    private final FullyQualifiedMethodNameWithClassName idFactory = new FullyQualifiedMethodNameWithClassName();
    private final PerftenceApi perftenceApi;

    public AbstractMultiThreadedTest() {
        this.perftenceApi = new DefaultPerftenceApiFactory().newPerftenceApi(failureNotifier());
    }

    /**
     * Rule used for tracking the test name
     */
    @Rule
    public TestName name = new TestName();

    /**
     * Uses fully qualified method name (with class name) as the name of the agent
     * test
     */
    protected final org.fluentjava.perftence.agents.TestBuilder agentBasedTest() {
        return agentBasedTest(id());
    }

    protected final org.fluentjava.perftence.agents.TestBuilder agentBasedTest(final String name) {
        return perftenceApi().agentBasedTest(name);
    }

    private PerftenceApi perftenceApi() {
        return this.perftenceApi;
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

    protected final org.fluentjava.perftence.fluent.TestBuilder test() {
        return test(id());
    }

    protected final org.fluentjava.perftence.fluent.TestBuilder test(String id) {
        return perftenceApi().test(id);
    }

    protected final String id() {
        return fullyQualifiedMethodNameWithClassName();
    }

    protected final String fullyQualifiedMethodNameWithClassName() {
        return idFactory().idFor(this.getClass(), testMethodName());
    }

    private FullyQualifiedMethodNameWithClassName idFactory() {
        return this.idFactory;
    }

    protected final String testMethodName() {
        return testName().getMethodName();
    }

    private TestName testName() {
        return this.name;
    }

    protected PerformanceRequirementsBuilder requirements() {
        return perftenceApi().requirements();
    }

    protected PerformanceTestSetupBuilder setup() {
        return perftenceApi().setup();
    }
}
