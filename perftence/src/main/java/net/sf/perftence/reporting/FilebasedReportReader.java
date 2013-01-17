package net.sf.perftence.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

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
    private final FailedInvocationsVisitor failedInvocationsVisitor;
    private final SummaryVisitor summaryVisitor;

    private DefaultThroughputStorage throughputStorage;

    private SetupReader setupReader;

    private FilebasedTestSetup setup;

    public FilebasedReportReader(final String id,
            final LatencyProvider latencyProvider,
            final InvocationStorage invocationStorage,
            final FailedInvocationsFactory failedInvocations) {
        this.reportDir = new File(new File("target", "perftence"), id);
        this.latencyVisitor = new LatencyFileVisitor(latencyProvider,
                invocationStorage);
        this.failedInvocationsVisitor = new FailedInvocationsVisitor(
                failedInvocations.newInstance());
        this.summaryVisitor = new SummaryVisitor();
        this.setupReader = new SetupReader();
    }

    final class SetupReader {

        public FilebasedTestSetup read() {
            try {
                final FileInputStream input = new FileInputStream(new File(
                        reportDirectory(), "setup"));
                final ObjectInputStream inputStream = new ObjectInputStream(
                        input);
                try {
                    return (FilebasedTestSetup) inputStream.readObject();
                } finally {
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    final static class FilebasedReader {
        private final LineVisitor visitor;
        private final LineReader reader;
        private String file;
        private final File root;

        public FilebasedReader(final LineVisitor lineVisitor,
                final String file, final File root) {
            this.file = file;
            this.root = root;
            this.reader = new LineReader();
            this.visitor = lineVisitor;
        }

        private LineReader reader() {
            return this.reader;
        }

        private LineVisitor lineVisitor() {
            return this.visitor;
        }

        public void read() throws FileNotFoundException, IOException {
            final FileInputStream setupStream = new FileInputStream(new File(
                    root(), file()));
            try {
                reader().read(setupStream, lineVisitor());
            } finally {
                setupStream.close();
            }
        }

        private File root() {
            return this.root;
        }

        private String file() {
            return this.file;
        }
    }

    static class SummaryVisitor implements LineVisitor {

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

    static class FailedInvocationsVisitor implements LineVisitor {

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

    static class ThroughputVisitor implements LineVisitor {

        private final ThroughputStorage throughputStorage;

        public ThroughputVisitor(final ThroughputStorage throughputStorage) {
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

    static class SetupVisitor implements LineVisitor {

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

        private static int toInt(final String value) {
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

    static class LatencyFileVisitor implements LineVisitor {

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
            this.setup = setupReader().read();
            final FilebasedReader summaryReader = newFilebasedReader(
                    summaryVisitor(), "summary");
            summaryReader.read();

            final FilebasedReader failedInvocations = newFilebasedReader(
                    failedInvocationsVisitor(), "failed-invocations");
            failedInvocations.read();

            final FilebasedReader latencies = newFilebasedReader(
                    latencyVisitor(), "latencies");
            latencies.read();

            this.throughputStorage = new DefaultThroughputStorage(setup()
                    .testSetup().throughputRange());
            final ThroughputVisitor throughputVisitor = new ThroughputVisitor(
                    this.throughputStorage);
            final FilebasedReader throughput = newFilebasedReader(
                    throughputVisitor, "throughput");
            throughput.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SetupReader setupReader() {
        return this.setupReader;
    }

    public ThroughputStorage throughputStorage() {
        return this.throughputStorage;
    }

    private FilebasedReader newFilebasedReader(final LineVisitor lineVisitor,
            final String name) {
        return new FilebasedReader(lineVisitor, name, reportDirectory());
    }

    private FailedInvocationsVisitor failedInvocationsVisitor() {
        return this.failedInvocationsVisitor;
    }

    private SummaryVisitor summaryVisitor() {
        return this.summaryVisitor;
    }

    private LatencyFileVisitor latencyVisitor() {
        return this.latencyVisitor;
    }

    public FilebasedTestSetup setup() {
        return this.setup;
    }

    public FailedInvocations failedInvocations() {
        return failedInvocationsVisitor().failedInvocations();
    }

    public FilebasedSummary summary() {
        return summaryVisitor().summary();
    }

    private static long toLong(final String value) {
        return Long.parseLong(value);
    }

}
