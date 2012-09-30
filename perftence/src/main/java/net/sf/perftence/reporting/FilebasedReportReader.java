package net.sf.perftence.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.LineReader;
import net.sf.perftence.LineVisitor;
import net.sf.perftence.PerformanceTestSetup;
import net.sf.perftence.PerformanceTestSetupPojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilebasedReportReader {

    private final static Logger LOG = LoggerFactory
            .getLogger(FilebasedReportReader.class);

    private final File reportDir;
    private final LatencyFileVisitor latencyVisitor;
    private final LineReader latencyReader;

    private final SetupVisitor setupVisitor;
    private final LineReader setupReader;

    private final LineReader throughputReader;

    private final LineReader failedInvocationsReader;
    private final FailedInvocationsVisitor failedInvocationsVisitor;

    private final LineReader summaryReader;
    private final SummaryVisitor summaryVisitor;

    public FilebasedReportReader(final String id,
            final LatencyProvider latencyProvider,
            final InvocationStorage invocationStorage,
            final FailedInvocationsFactory failedInvocations) {
        this.reportDir = new File(new File("target", "perftence"), id);

        this.throughputReader = new LineReader();

        this.latencyVisitor = new LatencyFileVisitor(latencyProvider,
                invocationStorage);
        this.latencyReader = new LineReader();

        this.setupReader = new LineReader();
        this.setupVisitor = new SetupVisitor();

        this.failedInvocationsReader = new LineReader();
        this.failedInvocationsVisitor = new FailedInvocationsVisitor(
                failedInvocations.newInstance());

        this.summaryReader = new LineReader();
        this.summaryVisitor = new SummaryVisitor();

    }

    class SummaryVisitor implements LineVisitor {

        private FilebasedSummary summary;

        @Override
        public void visit(final String line) {
            final String[] split = line.split(":");
            this.summary = new FilebasedSummary(toLong(split[1]),
                    toLong(split[2]), toLong(split[3]));
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from failed-invocations file");
        }

        public FilebasedSummary summary() {
            return this.summary;
        }
    }

    class FailedInvocationsVisitor implements LineVisitor {

        private final FailedInvocations failedInvocations;

        public FailedInvocationsVisitor(
                final FailedInvocations failedInvocations) {
            this.failedInvocations = failedInvocations;
        }

        @Override
        public void visit(final String line) {
            this.failedInvocations.more(line);
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from failed-invocations file");
        }

        public FailedInvocations failedInvocations() {
            return this.failedInvocations;
        }
    }

    class ThroughputVisitor implements LineVisitor {

        private final DefaultThroughputStorage throughputStorage;

        public ThroughputVisitor(
                final DefaultThroughputStorage throughputStorage) {
            this.throughputStorage = throughputStorage;
        }

        @Override
        public void visit(final String line) {
            final String[] split = line.split(":");
            this.throughputStorage.store(toLong(split[0]),
                    Double.parseDouble(split[1]));
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from throughput file");
        }
    }

    class SetupVisitor implements LineVisitor {

        private FilebasedTestSetup setup;

        @Override
        public void visit(final String line) {
            final String[] values = line.split(":");
            final PerformanceTestSetup testSetup = PerformanceTestSetupPojo
                    .builder().duration(toInt(values[0]))
                    .threads(toInt(values[1])).invocations(toInt(values[2]))
                    .invocationRange(toInt(values[3]))
                    .throughputRange(toInt(values[4])).build();
            this.setup = new FilebasedTestSetup(testSetup,
                    Boolean.parseBoolean(values[5]));
        }

        private int toInt(final String value) {
            return Integer.parseInt(value);
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from throughput file");
        }

        public FilebasedTestSetup setup() {
            return this.setup;
        }
    }

    class LatencyFileVisitor implements LineVisitor {

        private final LatencyProvider latencyProvider;
        private final InvocationStorage invocationStorage;

        public LatencyFileVisitor(final LatencyProvider latencyProvider,
                final InvocationStorage invocationStorage) {
            this.latencyProvider = latencyProvider;
            this.invocationStorage = invocationStorage;
        }

        @Override
        public void visit(final String line) {
            final long latency = Long.parseLong(line);
            this.latencyProvider.addSample(latency);
            // FIXME:
            this.invocationStorage.store((int) latency);
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from latency file");
        }
    }

    private static Logger log() {
        return LOG;
    }

    private File reportDirectory() {
        return this.reportDir;
    }

    public void read() {
        try {
            final FileInputStream setupStream = new FileInputStream(new File(
                    reportDirectory(), "setup"));
            try {
                setupReader().read(setupStream, setupVisitor());
            } finally {
                setupStream.close();
            }

            final FileInputStream latencyStream = new FileInputStream(
                    (new File(reportDirectory(), "latencies")));
            try {
                latencyReader().read(latencyStream, latencyVisitor());
            } finally {
                latencyStream.close();
            }

            final ThroughputVisitor throughputVisitor = new ThroughputVisitor(
                    new DefaultThroughputStorage(setupVisitor().setup()
                            .testSetup().throughputRange()));
            final FileInputStream throughputStream = new FileInputStream(
                    new File(reportDirectory(), "throughput"));
            try {
                throughputReader().read(throughputStream, throughputVisitor);
            } finally {
                throughputStream.close();
            }

            final FileInputStream failedInvocationsStream = new FileInputStream(
                    new File(reportDirectory(), "failed-invocations"));
            try {
                failedInvocationsReader().read(failedInvocationsStream,
                        throughputVisitor);
            } finally {
                failedInvocationsStream.close();
            }

            final FileInputStream summaryStream = new FileInputStream(new File(
                    reportDirectory(), "summary"));
            try {
                summaryReader().read(summaryStream, summaryVisitor());
            } finally {
                failedInvocationsStream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SummaryVisitor summaryVisitor() {
        return this.summaryVisitor;
    }

    private LineReader summaryReader() {
        return this.summaryReader;
    }

    private LineReader failedInvocationsReader() {
        return this.failedInvocationsReader;
    }

    private LineReader throughputReader() {
        return this.throughputReader;
    }

    private LineReader latencyReader() {
        return this.latencyReader;
    }

    private LineReader setupReader() {
        return this.setupReader;
    }

    private SetupVisitor setupVisitor() {
        return this.setupVisitor;
    }

    private LatencyFileVisitor latencyVisitor() {
        return this.latencyVisitor;
    }

    public FilebasedTestSetup setup() {
        return setupVisitor().setup();
    }

    public FailedInvocations failedInvocations() {
        return this.failedInvocationsVisitor.failedInvocations();
    }

    public FilebasedSummary summary() {
        return summaryVisitor().summary();
    }

    private static long toLong(final String value) {
        return Long.parseLong(value);
    }

}
