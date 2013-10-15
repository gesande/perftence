package net.sf.perftence;

import net.sf.perftence.api.PerftenceApi;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class AbstractMultiThreadedTest {

	private static TestFailureNotifier failureNotifier;
	private final FullyQualifiedMethodNameWithClassName idFactory = new FullyQualifiedMethodNameWithClassName();
	private final PerftenceApi perftenceApi;

	public AbstractMultiThreadedTest() {
		final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart();
		final DefaultTestRuntimeReporterFactory testRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
				deps);
		this.perftenceApi = new PerftenceApi(failureNotifier(),
				testRuntimeReporterFactory, deps.lineChartAdapterProvider(),
				deps.scatterPlotAdapterProvider());
	}

	/**
	 * Rule used for tracking the test name
	 */
	@Rule
	public TestName name = new TestName();

	/**
	 * Uses fully qualified method name (with class name) as the name of the
	 * agent test
	 */
	protected final net.sf.perftence.agents.TestBuilder agentBasedTest() {
		return agentBasedTest(id());
	}

	protected final net.sf.perftence.agents.TestBuilder agentBasedTest(
			final String name) {
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

	protected final net.sf.perftence.fluent.TestBuilder test() {
		return test(id());
	}

	protected final net.sf.perftence.fluent.TestBuilder test(String id) {
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
