package net.sf.perftence.setup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.perftence.reporting.graph.GraphWriter;
import net.sf.perftence.reporting.summary.SummaryAppender;

public final class PerformanceTestSetupPojo implements PerformanceTestSetup,
        Serializable {
    private static final long serialVersionUID = -687991492884005033L;

    public static class PerformanceTestSetupBuilder {
        private int duration = -1;
        private int threads = 1;
        private int invocations = 0;
        private int invocationRange = 500;
        private int throughputRange = 2500;
        private final List<SummaryAppender> summaryAppenders = new ArrayList<SummaryAppender>();
        private final List<GraphWriter> graphWriters = new ArrayList<GraphWriter>();

        public PerformanceTestSetupBuilder threads(final int threads) {
            this.threads = threads;
            return this;
        }

        public PerformanceTestSetupBuilder invocations(final int invocations) {
            this.invocations = invocations;
            return this;
        }

        /**
         * Duration in milliseconds
         */
        public PerformanceTestSetupBuilder duration(final int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Upper range for the latency graph scale
         */
        public PerformanceTestSetupBuilder invocationRange(
                final int invocationRange) {
            this.invocationRange = invocationRange;
            return this;
        }

        /**
         * Upper range for the throughput graph scale
         */
        public PerformanceTestSetupBuilder throughputRange(
                final int throughputRange) {
            this.throughputRange = throughputRange;
            return this;
        }

        public PerformanceTestSetup build() {
            return new PerformanceTestSetupPojo(this.threads, this.invocations,
                    this.duration, this.invocationRange, this.throughputRange,
                    this.summaryAppenders, this.graphWriters);
        }

        public PerformanceTestSetupBuilder summaryAppender(
                final SummaryAppender... summaryAppenders) {
            for (final SummaryAppender appender : summaryAppenders) {
                this.summaryAppenders.add(appender);
            }
            return this;
        }

        public PerformanceTestSetupBuilder graphWriter(
                final GraphWriter... graphWriters) {
            for (final GraphWriter writer : graphWriters) {
                this.graphWriters.add(writer);
            }
            return this;
        }

        public PerformanceTestSetup noSetup() {
            return new PerformanceTestSetup() {

                @Override
                public int throughputRange() {
                    throw noTestSetupDefined();
                }

                @Override
                public int threads() {
                    throw noTestSetupDefined();
                }

                @Override
                public Collection<SummaryAppender> summaryAppenders() {
                    throw noTestSetupDefined();
                }

                @Override
                public int invocations() {
                    throw noTestSetupDefined();
                }

                @Override
                public int invocationRange() {
                    throw noTestSetupDefined();
                }

                @Override
                public Collection<GraphWriter> graphWriters() {
                    throw noTestSetupDefined();
                }

                @Override
                public int duration() {
                    throw noTestSetupDefined();
                }

                private RuntimeException noTestSetupDefined() {
                    return new NoTestSetupDefined(
                            "No test setup has been defined!");
                }
            };
        }
    }

    private final int threads;
    private final int invocations;
    private final int duration;
    private final int invocationRange;
    private final int throughputRange;
    private final Collection<SummaryAppender> summaryAppenders;
    private final Collection<GraphWriter> graphWriters;

    private PerformanceTestSetupPojo(final int threads, final int invocations,
            final int duration, final int invocationRange,
            final int throughputRange,
            final Collection<SummaryAppender> summaryAppenders,
            final Collection<GraphWriter> graphWriters) {
        this.threads = threads;
        this.invocations = invocations;
        this.duration = duration;
        this.invocationRange = invocationRange;
        this.throughputRange = throughputRange;
        this.summaryAppenders = summaryAppenders;
        this.graphWriters = graphWriters;
    }

    @Override
    public int duration() {
        return this.duration;
    }

    @Override
    public int threads() {
        return this.threads;
    }

    @Override
    public int invocations() {
        return this.invocations;
    }

    @Override
    public int invocationRange() {
        return this.invocationRange;
    }

    @Override
    public int throughputRange() {
        return this.throughputRange;
    }

    @Override
    public Collection<SummaryAppender> summaryAppenders() {
        return this.summaryAppenders;
    }

    public static PerformanceTestSetupBuilder builder() {
        return new PerformanceTestSetupBuilder();
    }

    @Override
    public Collection<GraphWriter> graphWriters() {
        return this.graphWriters;
    }

    @Override
    public String toString() {
        return "PerformanceTestSetupPojo [threads=" + this.threads
                + ", invocations=" + this.invocations + ", duration="
                + this.duration + ", invocationRange=" + this.invocationRange
                + ", throughputRange=" + this.throughputRange
                + ", summaryAppenders=" + this.summaryAppenders
                + ", graphWriters=" + this.graphWriters + "]";
    }
}
