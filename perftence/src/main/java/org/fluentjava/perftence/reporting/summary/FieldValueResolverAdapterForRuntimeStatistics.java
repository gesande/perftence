package org.fluentjava.perftence.reporting.summary;

import org.fluentjava.perftence.RuntimeStatisticsProvider;

public final class FieldValueResolverAdapterForRuntimeStatistics {

    private final RuntimeStatisticsProvider statistics;

    public FieldValueResolverAdapterForRuntimeStatistics(final RuntimeStatisticsProvider statistics) {
        this.statistics = statistics;
    }

    public FieldValueResolver<Long> forExecutionTime() {
        return new FieldValueResolver<Long>() {
            @Override
            public Long value() {
                return statistics().currentDuration();
            }
        };
    }

    private RuntimeStatisticsProvider statistics() {
        return this.statistics;
    }

}
