package org.fluentjava.perftence.fluent;

import java.util.ArrayList;
import java.util.List;

import org.fluentjava.perftence.AllowedErrors;
import org.fluentjava.perftence.AllowedExceptionOrErrorOccurredMessageBuilder;
import org.fluentjava.perftence.AllowedExceptions;
import org.fluentjava.perftence.Executable;
import org.fluentjava.perftence.LastSecondStatistics;
import org.fluentjava.perftence.LatencyFactory;
import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.LatencyProviderFactory;
import org.fluentjava.perftence.PerfTestFailureFactory;
import org.fluentjava.perftence.PerformanceRequirements;
import org.fluentjava.perftence.RunNotifier;
import org.fluentjava.perftence.Startable;
import org.fluentjava.perftence.StatisticsProvider;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import org.fluentjava.perftence.graph.LastSecondThroughput;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilder;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocations;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.LastSecondFailures;
import org.fluentjava.perftence.reporting.summary.LastSecondIntermediateStatisticsProvider;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.setup.PerformanceTestSetup;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(TestBuilder.class);

    private final InvocationRunner invocationRunner;
    private final List<Startable> startables = new ArrayList<>();
    private final RunNotifier runNotifier;
    private final AllowedExceptions allowedExceptions = new AllowedExceptions();
    private final AllowedErrors allowedErrors = new AllowedErrors();
    private final FailedInvocationsFactory failedInvocationsFactory;
    private final SummaryBuilderFactory summaryBuilderFactory;
    private final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory;
    private final LatencyFactory latencyFactory;
    private final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOrErrorOccurredMessageBuilder;
    private final PerfTestFailureFactory perfTestFailureFactory;
    private final LineChartAdapterProvider<?, ?> lineChartAdapterProvider;
    private final TestRuntimeReporterFactory reporterFactory;
    private final LatencyProviderFactory latencyProviderFactory;
    private final SummaryConsumer summaryConsumer;

    private PerformanceTestSetup setup;
    private PerformanceRequirements requirements;
    private boolean includeInvocationGraph = true;

    public TestBuilder(final InvocationRunner invocationRunner, final RunNotifier runNotifier,
            final SummaryBuilderFactory summaryBuilderFactory, final FailedInvocationsFactory failedInvocationsFactory,
            final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory, final LatencyFactory latencyFactory,
            final AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOccurredMessageBuilder,
            final PerfTestFailureFactory perfTestFailureFactory,
            final LineChartAdapterProvider<?, ?> lineChartAdapterProvider,
            final TestRuntimeReporterFactory reporterFactory, final LatencyProviderFactory latencyProviderFactory,
            final SummaryConsumer summaryConsumer) {
        this.invocationRunner = invocationRunner;
        this.runNotifier = runNotifier;
        this.adjustedFieldBuilderFactory = adjustedFieldBuilderFactory;
        this.latencyFactory = latencyFactory;
        this.allowedExceptionOrErrorOccurredMessageBuilder = allowedExceptionOccurredMessageBuilder;
        this.perfTestFailureFactory = perfTestFailureFactory;
        this.lineChartAdapterProvider = lineChartAdapterProvider;
        this.reporterFactory = reporterFactory;
        this.latencyProviderFactory = latencyProviderFactory;
        this.summaryConsumer = summaryConsumer;
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
        return newWorker(executable, latencyProviderFactory().newInstance(), setup(), requirements());
    }

    private LatencyProviderFactory latencyProviderFactory() {
        return this.latencyProviderFactory;
    }

    private MultithreadWorker newWorker(final Executable executable, final LatencyProvider latencyProvider,
            final PerformanceTestSetup setup, final PerformanceRequirements requirements) {
        final AdjustedFieldBuilder fieldBuilder = adjustedFieldBuilderFactory().newInstance();
        final FailedInvocations failedInvocations = failedInvocationsFactory().newInstance();
        final LastSecondStatistics lastSecondStats = new LastSecondStatistics(latencyProviderFactory());
        final LastSecondFailures lastSecondFailures = new LastSecondFailures(failedInvocationsFactory());
        final LastSecondThroughput lastSecondThroughput = new LastSecondThroughput(lineChartAdapterProvider());
        final LastSecondIntermediateStatisticsProvider lastSecondStatsProvider = newLastSecondStatsProvider(
                lastSecondStats, fieldBuilder, lastSecondThroughput);
        setup.graphWriters().add(lastSecondThroughput.graphWriterFor(invocationRunner().id()));
        return new MultithreadWorker(runtimeReporter(latencyProvider, failedInvocations), invocationRunner(), setup,
                latencyProvider, allowedExceptions(), allowedErrors(),
                newPerformanceRequirementValidator(requirements, latencyProvider),
                summaryBuilderFactory().overAllSummaryBuilder(setup, latencyProvider),
                summaryBuilderFactory().intermediateSummaryBuilder(setup, latencyProvider, failedInvocations,
                        lastSecondStatsProvider, lastSecondFailures),
                latencyFactory(), allowedExceptionOrErrorOccurredMessageBuilder(), perfTestFailureFactory(),
                this.summaryConsumer).customInvocationReporters(lastSecondStats)
                        .customFailureReporter(lastSecondFailures).executable(executable);
    }

    private LineChartAdapterProvider<?, ?> lineChartAdapterProvider() {
        return this.lineChartAdapterProvider;
    }

    private AllowedExceptionOrErrorOccurredMessageBuilder allowedExceptionOrErrorOccurredMessageBuilder() {
        return this.allowedExceptionOrErrorOccurredMessageBuilder;
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

    private TestRuntimeReporter runtimeReporter(final LatencyProvider latencyProvider,
            final FailedInvocations failedInvocations) {
        return reporterFactory().newRuntimeReporter(latencyProvider, includeInvocationGraph(), setup(),
                failedInvocations);
    }

    private TestRuntimeReporterFactory reporterFactory() {
        return this.reporterFactory;
    }

    private PerformanceRequirementValidator newPerformanceRequirementValidator(PerformanceRequirements requirements,
            StatisticsProvider statisticsProvider) {
        return new PerformanceRequirementValidator(requirements, statisticsProvider, perfTestFailureFactory());
    }

    private PerfTestFailureFactory perfTestFailureFactory() {
        return this.perfTestFailureFactory;
    }

    private static LastSecondIntermediateStatisticsProvider newLastSecondStatsProvider(
            final LastSecondStatistics statisticsProvider, final AdjustedFieldBuilder fieldBuilder,
            final LastSecondThroughput lastSecondThroughput) {
        return new LastSecondIntermediateStatisticsProvider(fieldBuilder, statisticsProvider, lastSecondThroughput);
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
        LOG.info("All startables finished.");
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

    public TestBuilder allowError(final Class<? extends Error> allowed) {
        allowedErrors().allow(allowed);
        return this;
    }

    private AllowedExceptions allowedExceptions() {
        return this.allowedExceptions;
    }

    private AllowedErrors allowedErrors() {
        return this.allowedErrors;
    }

}
