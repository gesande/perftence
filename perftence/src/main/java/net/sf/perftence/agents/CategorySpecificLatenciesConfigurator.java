package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CategorySpecificLatenciesConfigurator {
    private final static Logger LOG = LoggerFactory
            .getLogger(CategorySpecificLatenciesConfigurator.class);
    private final CategorySpecificLatencies latencies;
    private final List<TestTaskCategory> latencyGraphFor;
    private final CategorySpecificReporterFactory reporterFactory;
    private final ReporterFactoryForCategorySpecificLatencies invocationReporterFactory;

    public CategorySpecificLatenciesConfigurator(
            final CategorySpecificLatencies latencies,
            final CategorySpecificReporterFactory reporterFactory,
            final ReporterFactoryForCategorySpecificLatencies invocationReporterFactory) {
        this.latencies = latencies;
        this.reporterFactory = reporterFactory;
        this.invocationReporterFactory = invocationReporterFactory;
        this.latencyGraphFor = Collections
                .synchronizedList(new ArrayList<TestTaskCategory>());
    }

    public void latencyGraphFor(final TestTaskCategory[] categories) {
        registerLatencyGraphFor(categories);
        reportCategorySpecificLatencies();
    }

    private List<TestTaskCategory> latencyGraphFor() {
        return this.latencyGraphFor;
    }

    public void latencyGraphForAll() {
        latencyGraphFor().add(new TestTaskCategory() {
            @Override
            public String name() {
                return "all";
            }
        });
        latencyForAll();
    }

    private void registerLatencyGraphFor(final TestTaskCategory... categories) {
        for (final TestTaskCategory category : categories) {
            newCategorySpecificReporter(category);
            latencyGraphFor().add(category);
            log().debug("Added invocation reporter for {}", category);
        }
    }

    private static Logger log() {
        return LOG;
    }

    private void newCategorySpecificReporter(final TestTaskCategory category) {
        categorySpecificLatencies().register(
                category,
                reporterFactory().adapterFor(invocationReporterFactory(),
                        category));
    }

    private ReporterFactoryForCategorySpecificLatencies invocationReporterFactory() {
        return this.invocationReporterFactory;
    }

    private CategorySpecificReporterFactory reporterFactory() {
        return this.reporterFactory;
    }

    private void reportCategorySpecificLatencies() {
        categorySpecificLatencies().reportCategorySpecificLatencies();
    }

    private void latencyForAll() {
        categorySpecificLatencies().latencyForAll();
    }

    private CategorySpecificLatencies categorySpecificLatencies() {
        return this.latencies;
    }

}