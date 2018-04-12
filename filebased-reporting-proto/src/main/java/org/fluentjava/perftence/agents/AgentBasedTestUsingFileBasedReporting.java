package org.fluentjava.perftence.agents;

import org.fluentjava.perftence.AllowedExceptionOrErrorOccurredMessageBuilder;
import org.fluentjava.perftence.DefaultLatencyProviderFactory;
import org.fluentjava.perftence.LatencyFactory;
import org.fluentjava.perftence.LatencyProviderFactory;
import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.jfreechart.DatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryFieldFactory;
import org.fluentjava.perftence.reporting.summary.TestSummaryLoggerFactory;

public class AgentBasedTestUsingFileBasedReporting {
    private final TestFailureNotifier failureNotifier;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;
    private final TestSummaryLoggerFactory testSummaryLoggerFactory;
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final DatasetAdapterFactory datasetAdapterFactory;
    private final LatencyProviderFactory latencyProviderFactory;
    private final SummaryConsumer summaryConsumer;

    public AgentBasedTestUsingFileBasedReporting(final TestFailureNotifier failureNotifier,
            SummaryConsumer summaryConsumer) {
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
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.latencyProviderFactory = new DefaultLatencyProviderFactory();
    }

    public TestBuilder test(final String id) {
        final TestFailureNotifierDecorator notifierAdapter = newNotifierDecorator(failureNotifier());
        return new TestBuilder(id, notifierAdapter,
                new SummaryBuilderFactory(testSummaryLoggerFactory(), summaryFieldFactory(), notifierAdapter),
                failedInvocationsFactory(), latencyFactory(), allowedExceptionOccurredMessageBuilder(),
                adjustedFieldBuilderFactory(), TaskScheduleDifferences.instance(datasetAdapterFactory()),
                new SchedulingServiceFactory(),
                new DefaultCategorySpecificReporterFactory(id, latencyProviderFactory()), latencyProviderFactory(),
                TestRuntimeReporterFactoryUsingJFreeChart.reporterFactory(HtmlTestReport.withDefaultReportPath()),
                datasetAdapterFactory(), datasetAdapterFactory(), this.summaryConsumer);
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
