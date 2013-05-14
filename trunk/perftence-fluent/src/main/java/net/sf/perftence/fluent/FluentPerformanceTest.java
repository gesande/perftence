package net.sf.perftence.fluent;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.DefaultLatencyProviderFactory;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProviderFactory;
import net.sf.perftence.PerfTestFailureFactory;
import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.common.FailedInvocationsFactory;
import net.sf.perftence.common.TestRuntimeReporterFactory;
import net.sf.perftence.concurrent.ThreadEngineApi;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.formatting.DefaultDoubleFormatter;
import net.sf.perftence.formatting.FieldFormatter;
import net.sf.perftence.reporting.graph.DatasetAdapterFactory;
import net.sf.perftence.reporting.graph.jfreechart.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummaryLoggerFactory;
import net.sf.perftence.setup.PerformanceTestSetupPojo;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

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
    private final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final PerfTestFailureFactory perfTestFailureFactory;
    private final DatasetAdapterFactory datasetAdapterFactory;
    private final EstimatedInvocations estimatedInvocations;
    private final InvocationRunnerFactory invocationRunnerFactory;
    private final TestRuntimeReporterFactory reporterFactory;
    private final LatencyProviderFactory latencyProviderFactory;

    public FluentPerformanceTest(final TestFailureNotifier failureNotifier,
            final TestRuntimeReporterFactory reporterFactory,
            final RunNotifier runNotifier) {
        this.reporterFactory = reporterFactory;
        validate(failureNotifier);
        this.failureNotifier = failureNotifier;
        this.runNotifier = runNotifier;
        this.estimatedInvocations = new EstimatedInvocations();
        this.invocationRunnerFactory = new InvocationRunnerFactory(
                new ThreadEngineApi<Invocation>().threadNamePrefix("perf-test"));
        final FieldFormatter fieldFormatter = new FieldFormatter();
        final FieldAdjuster fieldAdjuster = new FieldAdjuster();
        this.summaryBuilderFactory = newSummaryBuilderFactory(fieldFormatter,
                fieldAdjuster);
        this.adjustedFieldBuilderFactory = newAdjustedFieldBuilderFactory(
                fieldFormatter, fieldAdjuster);
        this.failedInvocationsFactory = newFailedInvocationsFactory();
        this.latencyFactory = new LatencyFactory();
        this.allowedExceptionOccurredMessageBuilder = new AllowedExceptionOccurredMessageBuilder();
        this.perfTestFailureFactory = new PerfTestFailureFactory();
        this.datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        this.latencyProviderFactory = new DefaultLatencyProviderFactory();
    }

    private static AdjustedFieldBuilderFactory newAdjustedFieldBuilderFactory(
            final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new AdjustedFieldBuilderFactory(fieldFormatter, fieldAdjuster);
    }

    private SummaryBuilderFactory newSummaryBuilderFactory(
            final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new SummaryBuilderFactory(SummaryFieldFactory.create(
                fieldFormatter, fieldAdjuster), new TestSummaryLoggerFactory(),
                estimatedInvocations());
    }

    private EstimatedInvocations estimatedInvocations() {
        return this.estimatedInvocations;
    }

    private FailedInvocationsFactory newFailedInvocationsFactory() {
        return new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                adjustedFieldBuilderFactory().newInstance());
    }

    private static void validate(final TestFailureNotifier failureNotifier) {
        if (failureNotifier == null) {
            throw TestFailureNotifier.NOTIFIER_IS_NULL;
        }
    }

    public TestBuilder test(final String id) {
        return new TestBuilder(newInvocationRunner(id), runNotifier(),
                summaryBuilderFactory(), failedInvocationsFactory(),
                adjustedFieldBuilderFactory(), latencyFactory(),
                allowedExceptionOccurredMessageBuilder(),
                perfTestFailureFactory(), datasetAdapterFactory(),
                reporterFactory(), latencyProviderFactory());
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private TestRuntimeReporterFactory reporterFactory() {
        return this.reporterFactory;
    }

    private DatasetAdapterFactory datasetAdapterFactory() {
        return this.datasetAdapterFactory;
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

    private AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder() {
        return this.allowedExceptionOccurredMessageBuilder;
    }

    private LatencyFactory latencyFactory() {
        return this.latencyFactory;
    }

    private AdjustedFieldBuilderFactory adjustedFieldBuilderFactory() {
        return this.adjustedFieldBuilderFactory;
    }

    private InvocationRunner newInvocationRunner(final String id) {
        return invocationRunnerFactory().create(runNotifier(),
                failureNotifier(), id);
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
