package net.sf.perftence.reporting.summary;

import net.sf.perftence.RuntimeStatisticsProvider;

public final class LastSecondIntermediateStatisticsProvider implements
        CustomIntermediateSummaryProvider {
    private final AdjustedFieldBuilder fieldBuilder;
    private final RuntimeStatisticsProvider statistics;
    private final LastSecondThroughput lastSecondThroughput;

    public LastSecondIntermediateStatisticsProvider(
            final AdjustedFieldBuilder fieldBuilder,
            final RuntimeStatisticsProvider statistics,
            final LastSecondThroughput lastSecondThroughput) {
        this.fieldBuilder = fieldBuilder;
        this.statistics = statistics;
        this.lastSecondThroughput = lastSecondThroughput;
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
        final double currentThroughput = statistics().currentThroughput();
        reportThroughput(currentThroughput);
        summary.field(fieldBuilder().field("throughput:", currentThroughput)
                .asFormatted());
    }

    private void reportThroughput(final double throughput) {
        this.lastSecondThroughput.report(throughput);
    }

    private RuntimeStatisticsProvider statistics() {
        return this.statistics;
    }

    private AdjustedFieldBuilder fieldBuilder() {
        return this.fieldBuilder;
    }
}