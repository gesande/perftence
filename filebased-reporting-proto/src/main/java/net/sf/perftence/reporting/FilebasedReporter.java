package net.sf.perftence.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.PerftenceRuntimeException;
import net.sf.perftence.setup.PerformanceTestSetup;

public class FilebasedReporter implements TestRuntimeReporter {

    private final static Logger LOG = LoggerFactory.getLogger(FilebasedReporter.class);
    private final BufferedWriter latencyWriter;
    private final BufferedWriter throughputWriter;
    private final BufferedWriter failedInvocationWriter;
    private final boolean includeInvocationGraph;
    private final File reportDirectory;
    private SummaryFileWriter summaryFileWriter;

    public FilebasedReporter(final String id, final PerformanceTestSetup testSetup,
            final boolean includeInvocationGraph) {
        this.includeInvocationGraph = includeInvocationGraph;
        final File root = new File("target", "perftence");
        this.reportDirectory = new File(root, id);
        if (!reportDirectory().exists()) {
            if (!reportDirectory().mkdirs()) {
                throw new PerftenceRuntimeException(
                        "Not able to create directory '" + id + "' to directory '" + reportDirectory().getName() + "'");
            }
        } else {
            log().info("Directory '" + id + " already exists under '" + reportDirectory().getName() + "'...");
        }
        this.latencyWriter = newBufferedWriterFor("latencies");
        this.throughputWriter = newBufferedWriterFor("throughput");
        this.failedInvocationWriter = newBufferedWriterFor("failed-invocations");
        this.summaryFileWriter = new SummaryFileWriter();
        final TestSetupFileWriter testSetupFileWriter = new TestSetupFileWriter(
                new FilebasedTestSetup(testSetup, includeInvocationGraph));
        testSetupFileWriter.write();
    }

    private BufferedWriter newBufferedWriterFor(final String fileName) {
        try {
            File reportDirectory2 = reportDirectory();
            File file = new File(reportDirectory2, fileName);
            FileWriter out = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(out);
            return bufferedWriter;
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        }
    }

    final class TestSetupFileWriter {
        private final FilebasedTestSetup testSetup;

        public TestSetupFileWriter(FilebasedTestSetup testSetup) {
            this.testSetup = testSetup;
        }

        public void write() {
            try {
                final FileOutputStream output = new FileOutputStream(new File(reportDirectory(), "setup"));
                try {
                    final ObjectOutputStream outputStream = new ObjectOutputStream(output);
                    try {
                        outputStream.writeObject(setup());
                    } finally {
                        outputStream.close();
                    }
                } finally {
                    output.close();
                }
            } catch (IOException ex) {
                throw new PerftenceRuntimeException(ex);
            }
        }

        private FilebasedTestSetup setup() {
            return this.testSetup;
        }
    }

    @Override
    public synchronized void latency(final int latency) {
        try {
            ((BufferedWriter) latencyWriter().append(Integer.toString(latency))).newLine();
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        }
    }

    @Override
    public void summary(final String id, final long elapsedTime, final long sampleCount, final long startTime) {
        try {
            latencyWriter().close();
            throughputWriter().close();
            summaryFileWriter().write(id, elapsedTime, sampleCount, startTime);
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        }
    }

    private SummaryFileWriter summaryFileWriter() {
        return this.summaryFileWriter;
    }

    final class SummaryFileWriter {

        public void write(final String id, final long elapsedTime, final long sampleCount, final long startTime)
                throws IOException {
            final BufferedWriter bufferedWriter = newBufferedWriterFor("summary");
            try {
                bufferedWriter.append(SummaryLine.summaryLine(id, elapsedTime, sampleCount, startTime));
                bufferedWriter.newLine();
            } finally {
                bufferedWriter.close();
            }
        }

    }

    final static class SummaryLine {

        public static String summaryLine(final String id, final long elapsedTime, final long sampleCount,
                final long startTime) {
            final StringBuilder sb = new StringBuilder(id).append(":").append(longToString(elapsedTime)).append(":")
                    .append(longToString(sampleCount)).append(":").append(longToString(startTime));
            return sb.toString();
        }
    }

    @Override
    public synchronized void throughput(final long currentDuration, final double throughput) {
        try {
            ((BufferedWriter) throughputWriter().append(ThroughputLine.throughputLine(currentDuration, throughput)))
                    .newLine();
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        }
    }

    final static class ThroughputLine {
        public static String throughputLine(final long currentDuration, final double throughput) {
            return new StringBuilder(longToString(currentDuration)).append(":").append(Double.toString(throughput))
                    .toString();
        }
    }

    @Override
    public synchronized void invocationFailed(final Throwable t) {
        try {
            failedInvocationWriter().append(t.getClass().getName());
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        }
    }

    @Override
    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    private static String longToString(final long value) {
        return Long.toString(value);
    }

    private BufferedWriter throughputWriter() {
        return this.throughputWriter;
    }

    private BufferedWriter latencyWriter() {
        return this.latencyWriter;
    }

    private File reportDirectory() {
        return this.reportDirectory;
    }

    private BufferedWriter failedInvocationWriter() {
        return this.failedInvocationWriter;
    }

    private static Logger log() {
        return LOG;
    }

}
