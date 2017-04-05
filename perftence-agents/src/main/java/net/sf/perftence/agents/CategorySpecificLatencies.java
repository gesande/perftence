package net.sf.perftence.agents;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CategorySpecificLatencies {
	private final static Logger LOG = LoggerFactory
			.getLogger(CategorySpecificLatencies.class);

	private final CategorySpecificReporterFactory reporterFactory;
	private final Map<TestTaskCategory, InvocationReporterAdapter> categorySpecificReporters;

	private boolean createCategorySpecificReportersOnTheFly = true;

	private final ReporterFactoryForCategorySpecificLatencies invocationReporter;

	public CategorySpecificLatencies(
			final CategorySpecificReporterFactory reporterFactory,
			ReporterFactoryForCategorySpecificLatencies invocationReporter) {
		if (reporterFactory == null) {
			throw new NullPointerException("reportFactory is null");
		}
		this.reporterFactory = reporterFactory;
		if (invocationReporter == null) {
			throw new NullPointerException("invocationReporter is null");
		}
		this.invocationReporter = invocationReporter;
		this.categorySpecificReporters = Collections.synchronizedMap(
				new HashMap<TestTaskCategory, InvocationReporterAdapter>());
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

	public synchronized void reportLatencyFor(final int latency,
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

	private InvocationReporterAdapter newCategorySpecificReporter(
			final TestTaskCategory category) {
		return registerFor(category,
				reporterFactory().adapterFor(invocationReporter(), category));
	}

	private ReporterFactoryForCategorySpecificLatencies invocationReporter() {
		return this.invocationReporter;
	}

	private InvocationReporterAdapter registerFor(
			final TestTaskCategory category,
			final InvocationReporterAdapter adapterFor) {
		register(category, adapterFor);
		return adapterFor;
	}

	private CategorySpecificReporterFactory reporterFactory() {
		return this.reporterFactory;
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