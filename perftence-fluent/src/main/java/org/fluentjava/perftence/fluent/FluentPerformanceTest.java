package org.fluentjava.perftence.fluent;

import org.fluentjava.perftence.AllowedExceptionOrErrorOccurredMessageBuilder;
import org.fluentjava.perftence.DefaultLatencyProviderFactory;
import org.fluentjava.perftence.LatencyFactory;
import org.fluentjava.perftence.LatencyProviderFactory;
import org.fluentjava.perftence.PerfTestFailureFactory;
import org.fluentjava.perftence.RunNotifier;
import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryFieldFactory;
import org.fluentjava.perftence.reporting.summary.TestSummaryLoggerFactory;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

import net.sf.völundr.concurrent.ThreadEngineApi;

/**
 * Entry point class for fluent performance test
 */
public final class FluentPerformanceTest {

    private final RunNotifier runNotifier;
    private final TestFailureNotifier failureNotifier;
    private final SummaryBuilderFactory summaryBuilderFactory;
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final PerfTestFailureFactory perfTestFailureFactory;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;
    private final EstimatedInvocations estimatedInvocations;
    private final InvocationRunnerFactory invocationRunnerFactory;
    private final TestRuntimeReporterFactory reporterFactory;
    private final LatencyProviderFactory latencyProviderFactory;
    private final SummaryConsumer summaryConsumer;

    public FluentPerformanceTest(final TestFailureNotifier failureNotifier,
            final TestRuntimeReporterFactory reporterFactory, final RunNotifier runNotifier,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider, SummaryConsumer summaryConsumer) {
        this.reporterFactory = reporterFactory;
        this.summaryConsumer = summaryConsumer;
        validate(failureNotifier);
        this.failureNotifier = failureNotifier;
        this.runNotifier = runNotifier;
        this.estimatedInvocations = new EstimatedInvocations();
        this.invocationRunnerFactory = new InvocationRunnerFactory(
                new ThreadEngineApi<Invocation>().threadNamePrefix("perf-test"));
        final FieldFormatter fieldFormatter = new FieldFormatter();
        final FieldAdjuster fieldAdjuster = new FieldAdjuster();
        this.summaryBuilderFactory = newSummaryBuilderFactory(fieldFormatter, fieldAdjuster);
        this.adjustedFieldBuilderFactory = newAdjustedFieldBuilderFactory(fieldFormatter, fieldAdjuster);
        this.failedInvocationsFactory = newFailedInvocationsFactory();
        this.latencyFactory = new LatencyFactory();
        this.allowedExceptionOccurredMessageBuilder = new AllowedExceptionOrErrorOccurredMessageBuilder();
        this.perfTestFailureFactory = new PerfTestFailureFactory();
        this.lineChartAdapterProvider = lineChartAdapterProvider;
        this.latencyProviderFactory = new DefaultLatencyProviderFactory();
    }

    private static AdjustedFieldBuilderFactory newAdjustedFieldBuilderFactory(final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new AdjustedFieldBuilderFactory(fieldFormatter, fieldAdjuster);
    }

    private SummaryBuilderFactory newSummaryBuilderFactory(final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new SummaryBuilderFactory(SummaryFieldFactory.create(fieldFormatter, fieldAdjuster),
                new TestSummaryLoggerFactory(), estimatedInvocations());
    }

    private EstimatedInvocations estimatedInvocations() {
        return this.estimatedInvocations;
    }

    private FailedInvocationsFactory newFailedInvocationsFactory() {
        return new FailedInvocationsFactory(new DefaultDoubleFormatter(), adjustedFieldBuilderFactory().newInstance());
    }

    private static void validate(final TestFailureNotifier failureNotifier) {
        if (failureNotifier == null) {
            throw TestFailureNotifier.NOTIFIER_IS_NULL;
        }
    }

    public TestBuilder test(final String id) {
        return new TestBuilder(newInvocationRunner(id), runNotifier(), summaryBuilderFactory(),
                failedInvocationsFactory(), adjustedFieldBuilderFactory(), latencyFactory(),
                allowedExceptionOccurredMessageBuilder(), perfTestFailureFactory(), lineChartAdapterProvider(),
                reporterFactory(), latencyProviderFactory(), this.summaryConsumer);
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private TestRuntimeReporterFactory reporterFactory() {
        return this.reporterFactory;
    }

    private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.lineChartAdapterProvider;
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

    private AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder() {
        return this.allowedExceptionOccurredMessageBuilder;
    }

    private LatencyFactory latencyFactory() {
        return this.latencyFactory;
    }

    private AdjustedFieldBuilderFactory adjustedFieldBuilderFactory() {
        return this.adjustedFieldBuilderFactory;
    }

    private InvocationRunner newInvocationRunner(final String id) {
        return invocationRunnerFactory().create(runNotifier(), failureNotifier(), id);
    }

    private InvocationRunnerFactory invocationRunnerFactory() {
        return this.invocationRunnerFactory;
    }

    private RunNotifier runNotifier() {
        return this.runNotifier;
    }

    private TestFailureNotifier failureNotifier() {
        return this.failureNotifier;
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    private SummaryBuilderFactory summaryBuilderFactory() {
        return this.summaryBuilderFactory;
    }

    @SuppressWarnings("static-method")
    public PerformanceRequirementsBuilder requirements() {
        return PerformanceRequirementsPojo.builder();
    }

    @SuppressWarnings("static-method")
    public PerformanceTestSetupBuilder setup() {
        return PerformanceTestSetupPojo.builder();
    }

}
