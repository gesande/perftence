package net.sf.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.sf.perftence.RuntimeStatisticsProvider;

import org.junit.Test;

public class ExecutionTimePluginTest {

    @SuppressWarnings("static-method")
    @Test
    public void withRuntimeStatistics() {
        final SummaryFieldPlugin<Long> plugin = new ExecutionTimePlugin(
                summaryFieldFactory(),
                new FieldValueResolverAdapterForRuntimeStatistics(statistics())
                        .forExecutionTime());
        assertNotNull(plugin);
        final SummaryField<Long> field = plugin.field().build();
        assertNotNull(field);
        assertNotNull(field.name());
        assertNotNull(field.value());
        assertEquals(4444, field.value().longValue());
    }

    private static SummaryFieldFactory summaryFieldFactory() {
        return SummaryFieldFactory.create(new FieldFormatter(),
                new FieldAdjuster());
    }

    private static RuntimeStatisticsProvider statistics() {
        return new RuntimeStatisticsProvider() {

            @Override
            public long sampleCount() {
                throw shouldNotCallThis();
            }

            @Override
            public long percentileLatency(int percentile) {
                throw shouldNotCallThis();
            }

            @Override
            public long minLatency() {
                throw shouldNotCallThis();
            }

            @Override
            public long median() {
                throw shouldNotCallThis();
            }

            @Override
            public long maxLatency() {
                throw shouldNotCallThis();
            }

            @Override
            public boolean hasSamples() {
                throw shouldNotCallThis();
            }

            @Override
            public double currentThroughput() {
                throw shouldNotCallThis();
            }

            @Override
            public long currentDuration() {
                return 4444;
            }

            @Override
            public double averageLatency() {
                throw shouldNotCallThis();
            }
        };
    }

    private static RuntimeException shouldNotCallThis() {
        return new RuntimeException("should not call this!");
    }
}
