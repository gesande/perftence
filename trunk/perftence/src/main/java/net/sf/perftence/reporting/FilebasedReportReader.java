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
import net.sf.perftence.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilebasedReportReader {

    private final static Logger LOG = LoggerFactory
            .getLogger(FilebasedReportReader.class);

    private final File reportDir;
    private final LatencyFileVisitor latencyVisitor;
    private final ThroughputVisitor throughputVisitor;
    private final LineReader throughputReader;
    private final LineReader latencyReader;

    private LineReader setupReader;

    private SetupVisitor setupVisitor;

    public FilebasedReportReader(final String id) {
        final File root = new File("target", "perftence");
        this.reportDir = new File(root, id);
        this.latencyVisitor = new LatencyFileVisitor(new LatencyProvider());
        this.throughputVisitor = new ThroughputVisitor(
                new DefaultThroughputStorage(100));
        this.throughputReader = new LineReader();
        this.latencyReader = new LineReader();
        this.setupReader = new LineReader();
        this.setupVisitor = new SetupVisitor();
    }

    class ThroughputVisitor implements LineVisitor {

        private final DefaultThroughputStorage throughputStorage;

        public ThroughputVisitor(DefaultThroughputStorage throughputStorage) {
            this.throughputStorage = throughputStorage;
        }

        @Override
        public void visit(final String line) {
            final String[] split = line.split(":");
            long currentDuration = Long.parseLong(split[0]);
            double throughput = Double.parseDouble(split[1]);
            this.throughputStorage.store(currentDuration, throughput);
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from throughput file");
        }
    }

    class SetupVisitor implements LineVisitor {

        private PerformanceTestSetup setup;

        @Override
        public void visit(String line) {
            String[] values = line.split(":");
            PerformanceTestSetupBuilder builder = PerformanceTestSetupPojo
                    .builder();
            builder.duration(toInt(values[0]));
            builder.threads(toInt(values[1]));
            builder.invocations(toInt(values[2]));
            builder.invocationRange(toInt(values[3]));
            builder.throughputRange(toInt(values[4]));
            this.setup = builder.build();
        }

        private int toInt(final String value) {
            return Integer.parseInt(value);
        }

        @Override
        public void emptyLine() {
            log().warn("Ignored some empty lines from throughput file");
        }

    }

    class LatencyFileVisitor implements LineVisitor {

        private final LatencyProvider latencyProvider;

        public LatencyFileVisitor(LatencyProvider latencyProvider) {
            this.latencyProvider = latencyProvider;
        }

        @Override
        public void visit(final String line) {
            this.latencyProvider.addSample(Long.parseLong(line));
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
            final FileInputStream latencyStream = new FileInputStream(
                    (new File(reportDirectory(), "latencies")));
            try {
                this.latencyReader.read(latencyStream, latencyVisitor());
            } finally {
                latencyStream.close();
            }

            final FileInputStream throughputStream = new FileInputStream(
                    new File(reportDirectory(), "throughput"));
            try {
                this.throughputReader.read(throughputStream,
                        throughputVisitor());
            } finally {
                throughputStream.close();
            }

            final FileInputStream setupStream = new FileInputStream(new File(
                    reportDirectory(), "setup"));
            try {
                this.setupReader.read(setupStream, setupVisitor());
            } finally {
                setupStream.close();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SetupVisitor setupVisitor() {
        return this.setupVisitor;
    }

    private ThroughputVisitor throughputVisitor() {
        return this.throughputVisitor;
    }

    private LatencyFileVisitor latencyVisitor() {
        return this.latencyVisitor;
    }

}
