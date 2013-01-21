package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.perftence.LatencyProvider;
import net.sf.perftence.reporting.InvocationReporter;

import org.junit.Test;

public class CategorySpecificLatenciesTest implements
        InvocationReporterFactoryForCategorySpecificLatencies {

    @Test
    public void empty() {
        assertFalse(new CategorySpecificLatencies("name", this)
                .hasCategorySpecificReporters());
    }

    @Test
    public void oneAppender() {
        CategorySpecificLatencies categorySpecificLatencies = new CategorySpecificLatencies(
                "name", this);
        assertFalse(categorySpecificLatencies.hasCategorySpecificReporters());
        categorySpecificLatencies.newCategorySpecificReporter(Category.One);

        assertTrue(categorySpecificLatencies.hasCategorySpecificReporters());

        categorySpecificLatencies.startAdapters();
        categorySpecificLatencies.reportLatencyFor(1000, Category.One);
        categorySpecificLatencies.reportFailure(Category.One, new IFail());
        categorySpecificLatencies.stop();
        categorySpecificLatencies.summaryTime();
    }

    class IFail extends Exception {//
    }

    enum Category implements TestTaskCategory {
        One
    }

    @Override
    public InvocationReporter newInvocationReporter(
            final LatencyProvider latencyProvider, final int threads) {
        return new InvocationReporter() {

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
