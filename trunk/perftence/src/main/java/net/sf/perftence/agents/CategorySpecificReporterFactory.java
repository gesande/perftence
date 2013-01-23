package net.sf.perftence.agents;

public interface CategorySpecificReporterFactory {
    InvocationReporterAdapter adapterFor(
            final ReporterFactoryForCategorySpecificLatencies reporterFactory,
            final TestTaskCategory category);
}
