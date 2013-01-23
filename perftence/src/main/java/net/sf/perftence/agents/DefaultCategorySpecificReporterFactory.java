package net.sf.perftence.agents;

import net.sf.perftence.LatencyProvider;

public final class DefaultCategorySpecificReporterFactory implements
        CategorySpecificReporterFactory {

    private final String name;

    public DefaultCategorySpecificReporterFactory(final String name) {
        this.name = name;
    }

    @Override
    public InvocationReporterAdapter adapterFor(
            final ReporterFactoryForCategorySpecificLatencies reporterFactory,
            final TestTaskCategory category) {
        final LatencyProvider latencyProvider = new LatencyProvider();
        return new InvocationReporterAdapter(name(), latencyProvider, category,
                reporterFactory.newReporter(latencyProvider, 0));
    }

    private String name() {
        return this.name;
    }

}
