package org.fluentjava.perftence.agents;

import org.fluentjava.perftence.AllowedExceptionOrErrorOccurredMessageBuilder;
import org.fluentjava.perftence.LatencyFactory;
import org.fluentjava.perftence.LatencyProviderFactory;
import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.graph.ScatterPlotAdapterProvider;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryFieldFactory;
import org.fluentjava.perftence.reporting.summary.TestSummaryLoggerFactory;

/**
 * Entry point class for agent based test.
 */
public final class AgentBasedTest {
    private final TestFailureNotifier failureNotifier;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;
    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;
    private final LatencyProviderFactory latencyProviderFactory;
    private final TestRuntimeReporterFactory testRuntimeReporterFactory;
    private final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider;
    private final SummaryConsumer summaryConsumer;

    public AgentBasedTest(final TestFailureNotifier failureNotifier,
            final LatencyProviderFactory latencyProviderFactory,
            final TestRuntimeReporterFactory testRuntimeReporterFactory,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider,
            final ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider, final SummaryConsumer summaryConsumer) {
        this.scatterPlotAdapterProvider = scatterPlotAdapterProvider;
        this.summaryConsumer = summaryConsumer;
        this.failureNotifier = validate(failureNotifier);
        final FieldFormatter fieldFormatter = new FieldFormatter();
        final FieldAdjuster fieldAdjuster = new FieldAdjuster();
        this.summaryFieldFactory = new SummaryFieldFactoryForAgentBasedTests(
                SummaryFieldFactory.create(fieldFormatter, fieldAdjuster));
        this.testSummaryLoggerFactory = new TestSummaryLoggerFactory();
        this.adjustedFieldBuilderFactory = new AdjustedFieldBuilderFactory(fieldFormatter, fieldAdjuster);
        this.failedInvocationsFactory = new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                adjustedFieldBuilderFactory().newInstance());
        this.latencyFactory = new LatencyFactory();
        this.allowedExceptionOccurredMessageBuilder = new AllowedExceptionOrErrorOccurredMessageBuilder();
        this.lineChartAdapterProvider = lineChartAdapterProvider;
        this.latencyProviderFactory = latencyProviderFactory;
        this.testRuntimeReporterFactory = testRuntimeReporterFactory;
    }

    public TestBuilder test(final String id) {
        final TestFailureNotifierDecorator notifierDecorator = newNotifierDecorator(failureNotifier());
        return new TestBuilder(id, notifierDecorator,
                new SummaryBuilderFactory(testSummaryLoggerFactory(), summaryFieldFactory(), notifierDecorator),
                failedInvocationsFactory(), latencyFactory(), allowedExceptionOccurredMessageBuilder(),
                adjustedFieldBuilderFactory(), TaskScheduleDifferences.instance(lineChartAdapterProvider()),
                new SchedulingServiceFactory(),
                new DefaultCategorySpecificReporterFactory(id, latencyProviderFactory()), latencyProviderFactory(),
                testRuntimeReporterFactory(), lineChartAdapterProvider(), scatterPlotAdapterProvider(),
                this.summaryConsumer);
    }

    private ScatterPlotAdapterProvider<?, ?> scatterPlotAdapterProvider() {
        return this.scatterPlotAdapterProvider;
    }

    private TestRuntimeReporterFactory testRuntimeReporterFactory() {
        return this.testRuntimeReporterFactory;
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.lineChartAdapterProvider;
    }

    private AdjustedFieldBuilderFactory adjustedFieldBuilderFactory() {
        return this.adjustedFieldBuilderFactory;
    }

    private static TestFailureNotifierDecorator newNotifierDecorator(final TestFailureNotifier failureNotifier) {
        return new TestFailureNotifierDecorator(failureNotifier);
    }

    private static TestFailureNotifier validate(final TestFailureNotifier failureNotifier) {
        if (failureNotifier == null) {
            throw TestFailureNotifier.NOTIFIER_IS_NULL;
        }
        return failureNotifier;
    }

    private TestFailureNotifier failureNotifier() {
        return this.failureNotifier;
    }

    private SummaryFieldFactoryForAgentBasedTests summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private TestSummaryLoggerFactory testSummaryLoggerFactory() {
        return this.testSummaryLoggerFactory;
    }

    private AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder() {
        return this.allowedExceptionOccurredMessageBuilder;
    }

    private LatencyFactory latencyFactory() {
        return this.latencyFactory;
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }
}
