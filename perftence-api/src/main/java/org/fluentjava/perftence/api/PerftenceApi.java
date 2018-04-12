package org.fluentjava.perftence.api;

import org.fluentjava.perftence.DefaultLatencyProviderFactory;
import org.fluentjava.perftence.LatencyProviderFactory;
import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.agents.AgentBasedTest;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.fluent.DefaultRunNotifier;
import org.fluentjava.perftence.fluent.FluentPerformanceTest;
import org.fluentjava.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

public final class PerftenceApi {
    private final FluentPerformanceTest performanceTest;
    private final AgentBasedTest agentBasedTest;
    private final TestFailureNotifier notifier;
    private final LatencyProviderFactory latencyProviderFactory;
    private final TestRuntimeReporterFactory testRuntimeReporterFactory;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;
    private final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider;
    private final SummaryConsumer summaryConsumer;

    public PerftenceApi(final TestFailureNotifier notifier, final TestRuntimeReporterFactory testRuntimeReporterFactory,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider,
            final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider, final SummaryConsumer summaryConsumer) {
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
        return new AgentBasedTest(failureNotifier(), latencyProviderFactory(), testRuntimeReporterFactory(),
                lineChartAdapterProvider(), this.scatterPlotAdapterProvider, this.summaryConsumer);
    }

    private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.lineChartAdapterProvider;
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private FluentPerformanceTest createPerformanceTest() {
        return new FluentPerformanceTest(failureNotifier(), testRuntimeReporterFactory(), new DefaultRunNotifier(),
                lineChartAdapterProvider(), this.summaryConsumer);
    }

    private TestRuntimeReporterFactory testRuntimeReporterFactory() {
        return this.testRuntimeReporterFactory;
    }

    public org.fluentjava.perftence.agents.TestBuilder agentBasedTest(final String name) {
        return agentBasedTest().test(name);
    }

    public org.fluentjava.perftence.fluent.TestBuilder test(final String name) {
        return performanceTest().test(name);
    }

    public PerformanceRequirementsBuilder requirements() {
        return performanceTest().requirements();
    }

    public PerformanceTestSetupBuilder setup() {
        return performanceTest().setup();
    }

}
