package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.LatencyProviderFactory;

final class DefaultCategorySpecificReporterFactory implements
		CategorySpecificReporterFactory {

	private final String name;
	private final LatencyProviderFactory latencyProviderFactory;

	public DefaultCategorySpecificReporterFactory(final String name,
			final LatencyProviderFactory latencyProviderFactory) {
		this.name = name;
		this.latencyProviderFactory = latencyProviderFactory;
	}

	@Override
	public InvocationReporterAdapter adapterFor(
			final ReporterFactoryForCategorySpecificLatencies reporterFactory,
			final TestTaskCategory category) {
		final LatencyProvider latencyProvider = latencyProviderFactory()
				.newInstance();
		return new InvocationReporterAdapter(name(), latencyProvider, category,
				reporterFactory.newReporter(latencyProvider, 0));
	}

	private LatencyProviderFactory latencyProviderFactory() {
		return this.latencyProviderFactory;
	}

	private String name() {
		return this.name;
	}

}
