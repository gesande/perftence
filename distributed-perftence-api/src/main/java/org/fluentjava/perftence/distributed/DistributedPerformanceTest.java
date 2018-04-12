package org.fluentjava.perftence.distributed;

import java.util.concurrent.ExecutorService;

import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.RunNotifier;
import org.fluentjava.perftence.TestFailureNotifier;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.common.InvocationStorage;
import org.fluentjava.perftence.common.TestRuntimeReporterFactory;
import org.fluentjava.perftence.common.ThroughputStorage;
import org.fluentjava.perftence.fluent.FluentPerformanceTest;
import org.fluentjava.perftence.fluent.TestBuilder;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.reporting.TestRuntimeReporter;
import org.fluentjava.perftence.reporting.summary.FailedInvocations;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryToCsv.CsvSummary;
import org.fluentjava.perftence.setup.PerformanceTestSetup;

public final class DistributedPerformanceTest {

    private final String id;
    private final TestFailureNotifier testFailureNotifier;
    private final ExecutorService executorService;
    private final RunNotifier runNotifier;
    private final TestRuntimeReporterFactory defaultReporterFactory;
    private final RemoteLatencyReporter remoteReporter;

    public DistributedPerformanceTest(final String id, final TestFailureNotifier testFailureNotifier,
            final ExecutorService executorService, final RunNotifier runNotifier,
            final RemoteLatencyReporter remoteReporter) {
        this.id = id;
        this.testFailureNotifier = testFailureNotifier;
        this.executorService = executorService;
        this.runNotifier = runNotifier;
        this.remoteReporter = remoteReporter;
        this.defaultReporterFactory = TestRuntimeReporterFactoryUsingJFreeChart
                .reporterFactory(HtmlTestReport.withDefaultReportPath());
    }

    public TestBuilder setup(final PerformanceTestSetup setup) {
        final TestRuntimeReporterFactory reporterFactory = new TestRuntimeReporterFactory() {
            @Override
            public TestRuntimeReporter newRuntimeReporter(LatencyProvider latencyProvider,
                    boolean includeInvocationGraph, PerformanceTestSetup setup, FailedInvocations failedInvocations) {
                return new DistributedTestRuntimeReporter(defaultReporterFactory().newRuntimeReporter(latencyProvider,
                        includeInvocationGraph, setup, failedInvocations), executorService(), remoteReporter());
            }

            @Override
            public TestRuntimeReporter newRuntimeReporter(LatencyProvider latencyProvider,
                    boolean includeInvocationGraph, PerformanceTestSetup setup, FailedInvocations failedInvocations,
                    InvocationStorage invocationStorage, ThroughputStorage throughputStorage) {
                return defaultReporterFactory().newRuntimeReporter(latencyProvider, includeInvocationGraph, setup,
                        failedInvocations, invocationStorage, throughputStorage);
            }

        };
        return new FluentPerformanceTest(this.testFailureNotifier, reporterFactory, this.runNotifier,
                new DefaultDatasetAdapterFactory(), new SummaryConsumer() {

                    @Override
                    public void consumeSummary(String summaryId, CsvSummary convertToCsv) {
                        // no impl
                    }

                    @Override
                    public void consumeSummary(String summaryId, String summary) {
                        // no impl
                    }

                }).test(id()).setup(setup);
    }

    private ExecutorService executorService() {
        return this.executorService;
    }

    private String id() {
        return this.id;
    }

    private TestRuntimeReporterFactory defaultReporterFactory() {
        return this.defaultReporterFactory;
    }

    private RemoteLatencyReporter remoteReporter() {
        return this.remoteReporter;
    }
}