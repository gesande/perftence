package net.sf.perftence.agents;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.DefaultLatencyProviderFactory;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProviderFactory;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.common.FailedInvocationsFactory;
import net.sf.perftence.formatting.DefaultDoubleFormatter;
import net.sf.perftence.formatting.FieldFormatter;
import net.sf.perftence.graph.jfreechart.DatasetAdapterFactory;
import net.sf.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummaryLoggerFactory;

public class AgentBasedTestUsingFileBasedReporting {
    private final TestFailureNotifier failureNotifier;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;
    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final DatasetAdapterFactory datasetAdapterFactory;
    private final LatencyProviderFactory latencyProviderFactory;

    public AgentBasedTestUsingFileBasedReporting(
            final TestFailureNotifier failureNotifier) {
        this.failureNotifier = validate(failureNotifier);
        final FieldFormatter fieldFormatter = new FieldFormatter();
        final FieldAdjuster fieldAdjuster = new FieldAdjuster();
        this.summaryFieldFactory = new SummaryFieldFactoryForAgentBasedTests(
                SummaryFieldFactory.create(fieldFormatter, fieldAdjuster));
        this.testSummaryLoggerFactory = new TestSummaryLoggerFactory();
        this.adjustedFieldBuilderFactory = new AdjustedFieldBuilderFactory(
                fieldFormatter, fieldAdjuster);
        this.failedInvocationsFactory = new FailedInvocationsFactory(
                new DefaultDoubleFormatter(), adjustedFieldBuilderFactory()
                        .newInstance());
        this.latencyFactory = new LatencyFactory();
        this.allowedExceptionOccurredMessageBuilder = new AllowedExceptionOccurredMessageBuilder();
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.latencyProviderFactory = new DefaultLatencyProviderFactory();
    }

    public TestBuilder test(final String id) {
        final TestFailureNotifierDecorator notifierAdapter = newNotifierDecorator(failureNotifier());
        return new TestBuilder(id, notifierAdapter, new SummaryBuilderFactory(
                testSummaryLoggerFactory(), summaryFieldFactory(),
                notifierAdapter), failedInvocationsFactory(), latencyFactory(),
                allowedExceptionOccurredMessageBuilder(),
                adjustedFieldBuilderFactory(),
                TaskScheduleDifferences.instance(datasetAdapterFactory()),
                new SchedulingServiceFactory(),
                new DefaultCategorySpecificReporterFactory(id,
                        latencyProviderFactory()), datasetAdapterFactory(),
                latencyProviderFactory(),
                new DefaultTestRuntimeReporterFactory());
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    private AdjustedFieldBuilderFactory adjustedFieldBuilderFactory() {
        return this.adjustedFieldBuilderFactory;
    }

    private static TestFailureNotifierDecorator newNotifierDecorator(
            final TestFailureNotifier failureNotifier) {
        return new TestFailureNotifierDecorator(failureNotifier);
    }

    private static TestFailureNotifier validate(
            final TestFailureNotifier failureNotifier) {
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

    private AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder() {
        return this.allowedExceptionOccurredMessageBuilder;
    }

    private LatencyFactory latencyFactory() {
        return this.latencyFactory;
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

}
