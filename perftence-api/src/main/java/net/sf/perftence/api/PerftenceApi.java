package net.sf.perftence.api;

import net.sf.perftence.DefaultLatencyProviderFactory;
import net.sf.perftence.LatencyProviderFactory;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.agents.AgentBasedTest;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.fluent.DefaultRunNotifier;
import net.sf.perftence.fluent.FluentPerformanceTest;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.graph.ScatterPlotAdapterProvider;
import net.sf.perftence.reporting.summary.SummaryConsumer;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

public final class PerftenceApi {
	private final FluentPerformanceTest performanceTest;
	private final AgentBasedTest agentBasedTest;
	private final TestFailureNotifier notifier;
	private final LatencyProviderFactory latencyProviderFactory;
	private final TestRuntimeReporterFactory testRuntimeReporterFactory;
	private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;
	private final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider;
	private final SummaryConsumer summaryConsumer;

	public PerftenceApi(final TestFailureNotifier notifier,
			final TestRuntimeReporterFactory testRuntimeReporterFactory,
			final LineChartAdapterProvider<?, ?> lineChartAdapterProvider,
			final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider,
			final SummaryConsumer summaryConsumer) {
		this.notifier = notifier;
		this.lineChartAdapterProvider = lineChartAdapterProvider;
		this.scatterPlotAdapterProvider = scatterPlotAdapterProvider;
		this.summaryConsumer = summaryConsumer;
		this.latencyProviderFactory = new DefaultLatencyProviderFactory();
		this.testRuntimeReporterFactory = testRuntimeReporterFactory;
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
				testRuntimeReporterFactory(), lineChartAdapterProvider(),
				this.scatterPlotAdapterProvider, summaryConsumer);
	}

	private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
		return this.lineChartAdapterProvider;
	}

	private LatencyProviderFactory latencyProviderFactory() {
		return this.latencyProviderFactory;
	}

	private FluentPerformanceTest createPerformanceTest() {
		return new FluentPerformanceTest(failureNotifier(),
				testRuntimeReporterFactory(), new DefaultRunNotifier(),
				lineChartAdapterProvider(), summaryConsumer);
	}

	private TestRuntimeReporterFactory testRuntimeReporterFactory() {
		return this.testRuntimeReporterFactory;
	}

	public net.sf.perftence.agents.TestBuilder agentBasedTest(
			final String name) {
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
