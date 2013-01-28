package net.sf.perftence.reporting;

import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.reporting.graph.DefaultDatasetAdapterFactory;
import net.sf.perftence.reporting.summary.AdjustedFieldBuilderFactory;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.FieldFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class FilebasedReporterTest extends AbstractMultiThreadedTest {

    @Test
    public void write() {
        final AtomicInteger i = new AtomicInteger();
        final long now = System.currentTimeMillis();
        final PerformanceTestSetup testSetup = setup().threads(100)
                .invocations(10000).throughputRange(10000).build();
        final FilebasedReporter reporter = new FilebasedReporter(id(),
                testSetup, true);
        test().setup(testSetup).executable(new Executable() {
            @Override
            public void execute() throws Exception {
                final int value = i.incrementAndGet();
                reporter.latency(value);
                reporter.throughput(value, value);
            }
        }).start();
        reporter.summary(id(), 5000, 10000, now);

        final LatencyProvider latencyProvider = new LatencyProvider();
        final AdjustedFieldBuilderFactory adjustedFieldBuilderFactory = new AdjustedFieldBuilderFactory(
                new FieldFormatter(), new FieldAdjuster());
        final FailedInvocationsFactory failedInvocations = new FailedInvocationsFactory(
                new DefaultDoubleFormatter(),
                adjustedFieldBuilderFactory.newInstance());
        final DefaultDatasetAdapterFactory datasetAdapterFactory = new DefaultDatasetAdapterFactory();
        final InvocationStorage invocationStorage = InvocationStorageFactory
                .newDefaultInvocationStorage(10000, 10000,
                        datasetAdapterFactory);

        final FilebasedReportReader reader = new FilebasedReportReader(id(),
                latencyProvider, invocationStorage, failedInvocations,
                new ThroughputStorageFactory(datasetAdapterFactory));
        reader.read();

        final TestRuntimeReporter invocationReporter = DefaultInvocationReporterFactory
                .newDefaultInvocationReporter(latencyProvider, reader.setup()
                        .includeInvocationGraph(), reader.setup().testSetup(),
                        reader.failedInvocations(), invocationStorage, reader
                                .throughputStorage());

        invocationReporter.summary(id() + "-from-filebased", reader.summary()
                .elapsedTime(), reader.summary().sampleCount(), reader
                .summary().startTime());
    }
}
