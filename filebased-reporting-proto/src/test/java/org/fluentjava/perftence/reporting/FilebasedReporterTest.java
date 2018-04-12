package org.fluentjava.perftence.reporting;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import org.fluentjava.perftence.DefaultLatencyProviderFactory;
import org.fluentjava.perftence.Executable;
import org.fluentjava.perftence.LatencyProvider;
import org.fluentjava.perftence.common.DefaultInvocationStorage;
import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.common.InvocationStorage;
import org.fluentjava.perftence.common.ReportingOptionsFactory;
import org.fluentjava.perftence.common.ThroughputStorageFactory;
import org.fluentjava.perftence.formatting.DefaultDoubleFormatter;
import org.fluentjava.perftence.formatting.FieldFormatter;
import org.fluentjava.perftence.graph.jfreechart.DatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import org.fluentjava.perftence.junit.AbstractMultiThreadedTest;
import org.fluentjava.perftence.junit.DefaultTestRunner;
import org.fluentjava.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import org.fluentjava.perftence.reporting.summary.FailedInvocationsFactory;
import org.fluentjava.perftence.reporting.summary.FieldAdjuster;
import org.fluentjava.perftence.setup.PerformanceTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class FilebasedReporterTest extends AbstractMultiThreadedTest {

    @Test
    public void write() {
        final AtomicInteger i = new AtomicInteger();
        final long now = System.currentTimeMillis();
        final PerformanceTestSetup testSetup = setup().threads(100).invocations(10000).throughputRange(10000).build();
        final FilebasedReporter reporter = new FilebasedReporter(id(), testSetup, true);
        test().setup(testSetup).executable(new Executable() {
            @Override
            public void execute() throws Exception {
                final int value = i.incrementAndGet();
                reporter.latency(value);
                reporter.throughput(value, value);
            }
        }).start();
        reporter.summary(id(), 5000, 10000, now);

        final LatencyProvider latencyProvider = newLatencyProvider();
        final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory = new AdjustedFieldBuilderFactory(
                new FieldFormatter(), new FieldAdjuster());
        final FailedInvocationsFactory failedInvocations = new FailedInvocationsFactory(new DefaultDoubleFormatter(),
                adjustedFieldBuilderFactory.newInstance());
        final DefaultDatasetAdapterFactory datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        final InvocationStorage invocationStorage = newDefaultInvocationStorage(10000, 10000, datasetAdapterFactory);

        final FilebasedReportReader reader = new FilebasedReportReader(id(), latencyProvider, invocationStorage,
                failedInvocations, new ThroughputStorageFactory(datasetAdapterFactory),
                new File("target", "perftence"));
        reader.read();

        final TestRuntimeReporter invocationReporter = TestRuntimeReporterFactoryUsingJFreeChart
                .reporterFactory(HtmlTestReport.withDefaultReportPath()).newRuntimeReporter(latencyProvider,
                        reader.setup().includeInvocationGraph(), reader.setup().testSetup(), reader.failedInvocations(),
                        invocationStorage, reader.throughputStorage());

        invocationReporter.summary(id() + "-from-filebased", reader.summary().elapsedTime(),
                reader.summary().sampleCount(), reader.summary().startTime());
    }

    private static LatencyProvider newLatencyProvider() {
        return new DefaultLatencyProviderFactory().newInstance();
    }

    private static InvocationStorage newDefaultInvocationStorage(final int invocations, final int invocationRange,
            DatasetAdapterFactory datasetAdapterFactory) {
        return DefaultInvocationStorage.newDefaultStorage(invocations,
                ReportingOptionsFactory.latencyOptionsWithStatistics(invocationRange), datasetAdapterFactory);
    }

}
