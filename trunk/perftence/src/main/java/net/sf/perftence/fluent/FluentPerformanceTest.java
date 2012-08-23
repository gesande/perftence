package net.sf.perftence.fluent;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.PerfTestFailureFactory;
import net.sf.perftence.PerformanceTestSetupPojo;
import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;
import net.sf.perftence.RunNotifier;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.reporting.DefaultDoubleFormatter;
import net.sf.perftence.reporting.FailedInvocationsFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.FieldFormatter;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;
import net.sf.perftence.reporting.summary.TestSummaryLoggerFactory;

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

    public FluentPerformanceTest(final TestFailureNotifier failureNotifier) {
        validate(failureNotifier);
        this.failureNotifier = failureNotifier;
        this.runNotifier = new DefaultRunNotifier();
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
    }

    private static AdjustedFieldBuilderFactory newAdjustedFieldBuilderFactory(
            final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new AdjustedFieldBuilderFactory(fieldFormatter, fieldAdjuster);
    }

    private static SummaryBuilderFactory newSummaryBuilderFactory(
            final FieldFormatter fieldFormatter,
            final FieldAdjuster fieldAdjuster) {
        return new SummaryBuilderFactory(SummaryFieldFactory.create(
                fieldFormatter, fieldAdjuster), new TestSummaryLoggerFactory());
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
                perfTestFailureFactory());
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
        return InvocationRunnerFactory.create(runNotifier(), failureNotifier(),
                id);
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