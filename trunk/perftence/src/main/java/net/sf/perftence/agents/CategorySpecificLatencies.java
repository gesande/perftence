package net.sf.perftence.agents;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.InvocationReporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CategorySpecificLatencies {
    private static final Logger LOG = LoggerFactory
            .getLogger(CategorySpecificLatencies.class);

    private final InvocationReporterFactory reporterFactory;
    private final Map<TestTaskCategory, InvocationReporterAdapter> categorySpecificReporters;
    private final String name;

    private boolean createCategorySpecificReportersOnTheFly = true;

    public CategorySpecificLatencies(final String name,
            final InvocationReporterFactory reporterFactory) {
        this.reporterFactory = reporterFactory;
        this.categorySpecificReporters = Collections
                .synchronizedMap(new HashMap<TestTaskCategory, InvocationReporterAdapter>());
        this.name = name;
    }

    public void latencyForAll() {
        categorySpecificReporters().clear();
        this.createCategorySpecificReportersOnTheFly = true;
    }

    public void reportCategorySpecificLatencies() {
        this.createCategorySpecificReportersOnTheFly = false;
    }

    private Map<TestTaskCategory, InvocationReporterAdapter> categorySpecificReporters() {
        return this.categorySpecificReporters;
    }

    public void startAdapters() {
        log().debug("Starting reporter adapters...");
        for (InvocationReporterAdapter adapter : categorySpecificReporters()
                .values()) {
            adapter.start();
        }
        log().debug("Reporter adapters started.");
    }

    public void summaryTime() {
        if (hasCategorySpecificReporters()) {
            log().info("Creating test category specific reports...");
            for (final InvocationReporterAdapter adapter : categorySpecificReporters()
                    .values()) {
                adapter.summary();
            }
            log().info("Test category specific reports done.");
        }
    }

    public void stop() {
        if (hasCategorySpecificReporters()) {
            log().info("Stopping test category specific reports...");
            for (final InvocationReporterAdapter adapter : categorySpecificReporters()
                    .values()) {
                adapter.stop();
            }
            log().info("Test category specific reports done.");
        }
    }

    public boolean hasCategorySpecificReporters() {
        return !categorySpecificReporters().isEmpty();
    }

    public void register(final TestTaskCategory category,
            final InvocationReporterAdapter reporter) {
        categorySpecificReporters().put(category, reporter);
    }

    public synchronized void reportCategorySpecificLatency(final int latency,
            final TestTaskCategory category) {
        if (categorySpecificReporters().containsKey(category)) {
            reportLatency(categorySpecificReporters().get(category), latency);
        } else {
            if (createCategorySpecificReportersOnTheFly()) {
                reportLatency(newCategorySpecificReporter(category), latency);
            }
        }
    }

    private static void reportLatency(final InvocationReporterAdapter adapter,
            final int latency) {
        adapter.latency(latency);
    }

    public synchronized InvocationReporterAdapter newCategorySpecificReporter(
            final TestTaskCategory category) {
        final LatencyProvider counter = new LatencyProvider();
        final InvocationReporterAdapter reporter = new InvocationReporterAdapter(
                name(), counter, category, newInvocationReporter(counter, 0));
        register(category, reporter);
        return reporter;
    }

    private InvocationReporter newInvocationReporter(
            final LatencyProvider latencyProvider, final int threads) {
        return reporterFactory()
                .newInvocationReporter(latencyProvider, threads);
    }

    private InvocationReporterFactory reporterFactory() {
        return this.reporterFactory;
    }

    private String name() {
        return this.name;
    }

    private synchronized boolean createCategorySpecificReportersOnTheFly() {
        return this.createCategorySpecificReportersOnTheFly;
    }

    private static Logger log() {
        return LOG;
    }

    public void reportFailure(TestTaskCategory category, Throwable cause) {
        if (categorySpecificReporters().containsKey(category)) {
            reportFailure(categorySpecificReporters().get(category), cause);
        } else {
            if (createCategorySpecificReportersOnTheFly()) {
                reportFailure(newCategorySpecificReporter(category), cause);
            }
        }
    }

    private static void reportFailure(final InvocationReporterAdapter reporter,
            final Throwable cause) {
        reporter.invocationFailed(cause);
    }

}