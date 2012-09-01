package net.sf.perftence.reporting.summary;

import net.sf.perftence.RuntimeStatisticsProvider;

public final class LastSecondIntermediateStatisticsProvider implements
        CustomIntermediateSummaryProvider {
    private final AdjustedFieldBuilder fieldBuilder;
    private final RuntimeStatisticsProvider statistics;

    public LastSecondIntermediateStatisticsProvider(
            final AdjustedFieldBuilder fieldBuilder,
            final RuntimeStatisticsProvider statistics) {
        this.fieldBuilder = fieldBuilder;
        this.statistics = statistics;
    }

    @Override
    public void provideIntermediateSummary(final IntermediateSummary summary) {
        summary.text("Last second stats:").endOfLine();
        summary.field(fieldBuilder().field("samples:",
                statistics().sampleCount()));
        summary.field(fieldBuilder().field("max:", statistics().maxLatency()));
        summary.field(fieldBuilder().field("average:",
                statistics().averageLatency()).asFormatted());
        summary.field(fieldBuilder().field("median:", statistics().median()));
        summary.field(fieldBuilder().field("95 percentile:",
                statistics().percentileLatency(95)));
        summary.field(fieldBuilder().field("throughput:",
                statistics().currentThroughput()).asFormatted());
    }

    private RuntimeStatisticsProvider statistics() {
        return this.statistics;
    }

    private AdjustedFieldBuilder fieldBuilder() {
        return this.fieldBuilder;
    }
}