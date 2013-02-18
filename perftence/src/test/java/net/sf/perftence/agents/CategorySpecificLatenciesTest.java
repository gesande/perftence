package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.TestRuntimeReporter;

import org.junit.Test;

public final class CategorySpecificLatenciesTest implements
        ReporterFactoryForCategorySpecificLatencies {

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void reporterFactoryNull() {
        new CategorySpecificLatencies(null, this);
    }

    @SuppressWarnings({ "unused", "static-method" })
    @Test(expected = NullPointerException.class)
    public void invocationReporterNull() {
        new CategorySpecificLatencies(
                new DefaultCategorySpecificReporterFactory("id"), null);
    }

    @Test
    public void empty() {
        assertFalse(new CategorySpecificLatencies(
                new DefaultCategorySpecificReporterFactory("id"), this)
                .hasCategorySpecificReporters());
    }

    @Test
    public void oneAppender() {
        final DefaultCategorySpecificReporterFactory defaultCategorySpecificReporterFactory = new DefaultCategorySpecificReporterFactory(
                "name");
        final CategorySpecificLatencies categorySpecificLatencies = new CategorySpecificLatencies(
                defaultCategorySpecificReporterFactory, this);
        assertFalse(categorySpecificLatencies.hasCategorySpecificReporters());
        final Category categoryOne = Category.One;
        categorySpecificLatencies.register(categoryOne,
                defaultCategorySpecificReporterFactory.adapterFor(this,
                        categoryOne));
        assertTrue(categorySpecificLatencies.hasCategorySpecificReporters());
        categorySpecificLatencies.startAdapters();
        categorySpecificLatencies.reportLatencyFor(1000, categoryOne);
        categorySpecificLatencies.reportFailure(categoryOne, new IFail());
        categorySpecificLatencies.stop();
        categorySpecificLatencies.summaryTime();
    }

    class IFail extends Exception {//
    }

    enum Category implements TestTaskCategory {
        One
    }

    @Override
    public TestRuntimeReporter newReporter(
            final LatencyProvider latencyProvider, final int threads) {
        return new TestRuntimeReporter() {

            @Override
            public void throughput(final long currentDuration,
                    final double throughput) {
                throw new RuntimeException("Don't come here!");
            }

            @Override
            public void summary(final String id, final long elapsedTime,
                    final long sampleCount, final long startTime) {
                assertEquals("name-One-statistics", id);
                assertEquals(1, sampleCount);
            }

            @Override
            public void latency(final int latency) {
                assertEquals(1000, latency);
            }

            @Override
            public void invocationFailed(final Throwable t) {
                assertTrue(t.getClass().equals(IFail.class));
            }

            @Override
            public boolean includeInvocationGraph() {
                return true;
            }
        };
    }
}
