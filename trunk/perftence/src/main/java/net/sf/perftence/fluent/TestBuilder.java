package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.AllowedExceptionOccurredMessageBuilder;
import net.sf.perftence.AllowedExceptions;
import net.sf.perftence.Executable;
import net.sf.perftence.LastSecondStatistics;
import net.sf.perftence.LatencyFactory;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerfTestFailureFactory;
import net.sf.perftence.PerformanceRequirementValidator;
import net.sf.perftence.PerformanceRequirements;
import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.PerformanceTestSetupPojo;
import net.sf.perftence.RunNotifier;
import net.sf.perftence.Startable;
import net.sf.perftence.StatisticsProvider;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.reporting.FailedInvocations;
import net.sf.perftence.reporting.FailedInvocationsFactory;
import net.sf.perftence.reporting.InvocationReporter;
import net.sf.perftence.reporting.InvocationReporterFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilder;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import net.sf.perftence.reporting.summary.LastSecondFailures;
import net.sf.perftence.reporting.summary.LastSecondIntermediateStatisticsProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestBuilder {

    private static final Logger LOG = LoggerFactory
            .getLogger(TestBuilder.class);

    private final InvocationRunner invocationRunner;
    private final List<Startable> startables = new ArrayList<Startable>();
    private final RunNotifier runNotifier;
    private final AllowedExceptions allowedExceptions = new AllowedExceptions();
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final SummaryBuilderFactory summaryBuilderFactory;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder;
    private final PerfTestFailureFactory perfTestFailureFactory;

    private PerformanceTestSetup setup;
    private PerformanceRequirements requirements;
    private boolean includeInvocationGraph = true;

    public TestBuilder(
            final InvocationRunner invocationRunner,
            final RunNotifier runNotifier,
            final SummaryBuilderFactory summaryBuilderFactory,
            final FailedInvocationsFactory failedInvocationsFactory,
            final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory,
            final LatencyFactory latencyFactory,
            final AllowedExceptionOccurredMessageBuilder allowedExceptionOccurredMessageBuilder,
            final PerfTestFailureFactory perfTestFailureFactory) {
        this.invocationRunner = invocationRunner;
        this.runNotifier = runNotifier;
        this.adjustedFieldBuilderFactory = adjustedFieldBuilderFactory;
        this.latencyFactory = latencyFactory;
        this.allowedExceptionOccurredMessageBuilder = allowedExceptionOccurredMessageBuilder;
        this.perfTestFailureFactory = perfTestFailureFactory;
        PerformanceRequirementsPojo.builder();
        this.requirements = PerformanceRequirementsBuilder.noRequirements();
        this.setup = PerformanceTestSetupPojo.builder().noSetup();
        this.summaryBuilderFactory = summaryBuilderFactory;
        this.failedInvocationsFactory = failedInvocationsFactory;
    }

    public TestBuilder setup(final PerformanceTestSetup setup) {
        this.setup = setup;
        return this;
    }

    public TestBuilder requirements(final PerformanceRequirements requirements) {
        this.requirements = requirements;
        return this;
    }

    private InvocationRunner invocationRunner() {
        return this.invocationRunner;
    }

    public MultithreadWorker executable(final Executable executable) {
        return newWorker(executable, new LatencyProvider(), setup(),
                requirements());
    }

    private MultithreadWorker newWorker(final Executable executable,
            final LatencyProvider latencyProvider,
            final PerformanceTestSetup setup,
            final PerformanceRequirements requirements) {
        final AdjustedFieldBuilder fieldBuilder = adjustedFieldBuilderFactory()
                .newInstance();
        final FailedInvocations failedInvocations = failedInvocationsFactory()
                .newInstance();
        final LastSecondStatistics lastSecondStats = new LastSecondStatistics();
        final LastSecondFailures lastSecondFailures = new LastSecondFailures(
                failedInvocationsFactory());
        return new MultithreadWorker(invocationReporter(latencyProvider,
                failedInvocations), invocationRunner(), setup, latencyProvider,
                allowedExceptions(), newPerformanceRequirementValidator(
                        requirements, latencyProvider), summaryBuilderFactory()
                        .overAllSummaryBuilder(setup, latencyProvider),
                summaryBuilderFactory().intermediateSummaryBuilder(
                        setup,
                        latencyProvider,
                        failedInvocations,
                        newLastSecondStatsProvider(lastSecondStats,
                                fieldBuilder), lastSecondFailures),
                latencyFactory(), allowedExceptionOccurredMessageBuilder(),
                perfTestFailureFactory())
                .customInvocationReporters(lastSecondStats)
                .customFailureReporter(lastSecondFailures)
                .executable(executable);
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

    private SummaryBuilderFactory summaryBuilderFactory() {
        return this.summaryBuilderFactory;
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    private InvocationReporter invocationReporter(
            final LatencyProvider latencyProvider,
            final FailedInvocations failedInvocations) {
        return InvocationReporterFactory.newDefaultInvocationReporter(
                latencyProvider, includeInvocationGraph(), setup(),
                failedInvocations);
    }

    private PerformanceRequirementValidator newPerformanceRequirementValidator(
            PerformanceRequirements requirements,
            StatisticsProvider statisticsProvider) {
        return new PerformanceRequirementValidator(requirements,
                statisticsProvider, perfTestFailureFactory());
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

    private static CustomIntermediateSummaryProvider newLastSecondStatsProvider(
            final LastSecondStatistics statisticsProvider,
            final AdjustedFieldBuilder fieldBuilder) {
        return new LastSecondIntermediateStatisticsProvider(fieldBuilder,
                statisticsProvider);
    }

    private PerformanceTestSetup setup() {
        return this.setup;
    }

    private PerformanceRequirements requirements() {
        return this.requirements;
    }

    public TestBuilder startable(final MultithreadWorker worker) {
        this.startables.add(worker.startable());
        return this;
    }

    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    public void start() throws Exception {
        for (Startable s : this.startables) {
            s.start();
        }
        do {
            Thread.sleep(50);
        } while (!isFinished(this.startables));
        log().info("All startables finished.");
    }

    private static Logger log() {
        return LOG;
    }

    private boolean isFinished(final List<Startable> startables) {
        for (final Startable s : startables) {
            if (!runNotifier().isFinished(s.id())) {
                return false;
            }
        }
        return true;
    }

    private RunNotifier runNotifier() {
        return this.runNotifier;
    }

    public TestBuilder noInvocationGraph() {
        this.includeInvocationGraph = false;
        return this;
    }

    public TestBuilder allow(final Class<? extends Exception> allowed) {
        allowedExceptions().allow(allowed);
        return this;
    }

    private AllowedExceptions allowedExceptions() {
        return this.allowedExceptions;
    }

}
