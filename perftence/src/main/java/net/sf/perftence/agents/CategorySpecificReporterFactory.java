package net.sf.perftence.agents;

public interface CategorySpecificReporterFactory {
    InvocationReporterAdapter adapterFor(
            final InvocationReporterFactoryForCategorySpecificLatencies reporterFactory,
            final TestTaskCategory category);
}
