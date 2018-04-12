package org.fluentjava.perftence.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.fluentjava.perftence.graph.ImageData;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.reporting.ReportingOptions;
import org.junit.Test;

public class DefaultInvocationStorageTest {

    @Test
    public void empty() {
        assertTrue(newStorage(10, false).isEmpty());
    }

    @Test
    public void reportedLatencyBeingBelowOne() {
        final InvocationStorage storage = newStorage(10, false);
        assertTrue(storage.isEmpty());
        storage.store(0);
        assertFalse(storage.isEmpty());
        assertTrue(storage.reportedLatencyBeingBelowOne());
    }

    @Test
    public void statistics() {
        final InvocationStorage storage = newStorage(10, true);
        assertTrue(storage.isEmpty());
        storage.store(100);
        assertFalse(storage.isEmpty());
        assertFalse(storage.reportedLatencyBeingBelowOne());
        assertNotNull(storage.statistics());
    }

    @Test
    public void noStatistics() {
        final InvocationStorage storage = newStorage(10, false);
        assertTrue(storage.isEmpty());
        storage.store(100);
        assertFalse(storage.isEmpty());
        assertFalse(storage.reportedLatencyBeingBelowOne());
        assertNotNull(storage.statistics());
        assertNotNull(storage.imageData());
        assertNull(storage.imageData().statistics());
    }

    @Test
    public void imageData() {
        final InvocationStorage storage = newStorage(10, true);
        assertTrue(storage.isEmpty());
        storage.store(100);
        assertFalse(storage.isEmpty());
        assertFalse(storage.reportedLatencyBeingBelowOne());
        assertNotNull(storage.statistics());
        final ImageData imageData = storage.imageData();
        assertNotNull(imageData);
        assertNotNull(imageData.statistics());
    }

    private static InvocationStorage newStorage(final int totalInvocations, final boolean provideStatistics) {
        InvocationStorage storage = DefaultInvocationStorage.newDefaultStorage(totalInvocations,
                new ReportingOptions() {

                    @Override
                    public String xAxisTitle() {
                        return "xAxisTitle";
                    }

                    @Override
                    public String title() {
                        return "title";
                    }

                    @Override
                    public int range() {
                        return 10;
                    }

                    @Override
                    public boolean provideStatistics() {
                        return provideStatistics;
                    }

                    @Override
                    public String legendTitle() {
                        return "legendTitle";
                    }
                }, new DefaultDatasetAdapterFactory());
        return storage;
    }
}
