package net.sf.perftence.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.perftence.LatencyProvider;
import net.sf.perftence.LineReader;
import net.sf.perftence.LineVisitor;

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

    public FilebasedReportReader(final String id) {
        final File root = new File("target", "perftence");
        this.reportDir = new File(root, id);
        this.latencyVisitor = new LatencyFileVisitor(new LatencyProvider());
        this.throughputVisitor = new ThroughputVisitor(
                new DefaultThroughputStorage(100));
        this.throughputReader = new LineReader();
        this.latencyReader = new LineReader();

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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ThroughputVisitor throughputVisitor() {
        return this.throughputVisitor;
    }

    private LatencyFileVisitor latencyVisitor() {
        return this.latencyVisitor;
    }

}
