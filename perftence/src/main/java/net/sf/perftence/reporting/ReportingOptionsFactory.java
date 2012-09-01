package net.sf.perftence.reporting;

public final class ReportingOptionsFactory {

    private ReportingOptionsFactory() {
    }

    public static ReportingOptions latencyOptions(final int range) {
        return new ReportingOptions() {
            @Override
            public String title() {
                return "Latencies";
            }

            @Override
            public boolean provideStatistics() {
                return true;
            }

            @Override
            public String legendTitle() {
                return "Latency (ms)";
            }

            @Override
            public String xAxisTitle() {
                return "Invocations";
            }

            @Override
            public int range() {
                return range;
            }
        };
    }
}
