package net.sf.perftence.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CategorySpecificLatenciesConfigurator {
	private final static Logger LOG = LoggerFactory
			.getLogger(CategorySpecificLatenciesConfigurator.class);
	private final CategorySpecificLatencies latencies;
	private final CategorySpecificReporterFactory reporterFactory;
	private final ReporterFactoryForCategorySpecificLatencies invocationReporterFactory;

	public CategorySpecificLatenciesConfigurator(
			final CategorySpecificLatencies latencies,
			final CategorySpecificReporterFactory reporterFactory,
			final ReporterFactoryForCategorySpecificLatencies invocationReporterFactory) {
		this.latencies = latencies;
		this.reporterFactory = reporterFactory;
		this.invocationReporterFactory = invocationReporterFactory;
	}

	public void latencyGraphFor(final TestTaskCategory[] categories) {
		registerLatencyGraphFor(categories);
		reportCategorySpecificLatencies();
	}

	public void latencyGraphForAll() {
		latencyForAll();
	}

	private void registerLatencyGraphFor(final TestTaskCategory... categories) {
		for (final TestTaskCategory category : categories) {
			newCategorySpecificReporter(category);
			log().debug("Added invocation reporter for category '{}'", category);
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