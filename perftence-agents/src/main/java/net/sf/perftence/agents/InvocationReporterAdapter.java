package net.sf.perftence.agents;

import java.util.function.Function;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.SummaryConsumer;
import net.sf.perftence.reporting.summary.TestSummaryLogger;

public final class InvocationReporterAdapter {

    private final LatencyProvider latencyProvider;
    private final TestTaskCategory category;
    private final TestRuntimeReporter reporter;
    private boolean isStarted = false;
    private final String name;

    public InvocationReporterAdapter(final String name, final LatencyProvider latencyProvider,
            final TestTaskCategory category, final TestRuntimeReporter reporter) {
        this.name = name;
        this.latencyProvider = latencyProvider;
        this.category = category;
        this.reporter = reporter;
    }

    private LatencyProvider latencyProvider() {
        return this.latencyProvider;
    }

    private TestRuntimeReporter reporter() {
        return this.reporter;
    }

    private TestTaskCategory category() {
        return this.category;
    }

    public void summary() {
        reporter().summary(name() + "-" + category().name() + "-statistics", latencyProvider().duration(),
                latencyProvider().sampleCount(), latencyProvider().startTime());
    }

    private String name() {
        return this.name;
    }

    public synchronized void start() {
        latencyProvider().start();
        this.isStarted = true;
    }

    private synchronized boolean isStarted() {
        return this.isStarted;
    }

    public synchronized void latency(final int latency) {
        if (!isStarted()) {
            start();
        }
        latencyProvider().addSample(latency);
        reporter().latency(latency);
    }

    public void stop() {
        latencyProvider().stop();
    }

    public void invocationFailed(final Throwable cause) {
        reporter().invocationFailed(cause);
    }

    public void summaryForCategory(final Function<LatencyProvider, TestSummaryLogger> summaryBuilderFactory,
            SummaryConsumer summaryConsumer) {
        if (!isStarted())
            return;
        final TestSummaryLogger summaryLogger = summaryBuilderFactory.apply(latencyProvider());
        final String id = name() + "-" + category().name() + "-statistics.summary";
        summaryLogger.printSummary(id, summary -> summaryConsumer.consumeSummary(id, summary));
    }
}