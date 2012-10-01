package net.sf.perftence.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.perftence.PerformanceTestSetup;

public class FilebasedReporter implements InvocationReporter {

    private final BufferedWriter latencyWriter;
    private final BufferedWriter throughputWriter;
    private final BufferedWriter failedInvocationWriter;
    private final boolean includeInvocationGraph;
    private final File reportDir;
    private SummaryFileWriter summaryFileWriter;

    public FilebasedReporter(final String id,
            final PerformanceTestSetup testSetup,
            final boolean includeInvocationGraph) {

        this.includeInvocationGraph = includeInvocationGraph;
        final File root = new File("target", "perftence");
        this.reportDir = new File(root, id);
        reportDirectory().mkdirs();
        try {
            this.latencyWriter = newBufferedWriterFor("latencies");
            this.throughputWriter = newBufferedWriterFor("throughput");
            this.failedInvocationWriter = newBufferedWriterFor("failed-invocations");
            this.summaryFileWriter = new SummaryFileWriter();
            final TestSetupFileWriter testSetupFileWriter = new TestSetupFileWriter(
                    new FilebasedTestSetup(testSetup, includeInvocationGraph));
            testSetupFileWriter.write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter newBufferedWriterFor(final String fileName) {
        try {
            return new BufferedWriter(new FileWriter(new File(
                    reportDirectory(), fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final class TestSetupFileWriter {
        private final FilebasedTestSetup testSetup;

        public TestSetupFileWriter(FilebasedTestSetup testSetup) {
            this.testSetup = testSetup;
        }

        public void write() throws IOException {
            final BufferedWriter writer = newBufferedWriterFor("setup");
            try {
                
                writer.append(integerToString(setup().testSetup().duration()));
                writer.append(":");
                writer.append(integerToString(setup().testSetup().threads()));
                writer.append(":");
                writer.append(integerToString(setup().testSetup().invocations()));
                writer.append(":");
                writer.append(integerToString(setup().testSetup()
                        .invocationRange()));
                writer.append(":");
                writer.append(integerToString(setup().testSetup()
                        .throughputRange()));
                writer.append(":");
                writer.append(Boolean.toString(includeInvocationGraph()));
            } finally {
                writer.close();
            }
        }

        private boolean includeInvocationGraph() {
            return setup().includeInvocationGraph();
        }

        private FilebasedTestSetup setup() {
            return this.testSetup;
        }
    }

    @Override
    public synchronized void latency(final int latency) {
        try {
            ((BufferedWriter) latencyWriter().append(Integer.toString(latency)))
                    .newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void summary(final String id, final long elapsedTime,
            final long sampleCount, final long startTime) {
        try {
            latencyWriter().close();
            throughputWriter().close();
            summaryFileWriter().write(id, elapsedTime, sampleCount, startTime);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SummaryFileWriter summaryFileWriter() {
        return this.summaryFileWriter;
    }

    final class SummaryFileWriter {

        public void write(final String id, final long elapsedTime,
                final long sampleCount, final long startTime)
                throws IOException {
            final BufferedWriter bufferedWriter = newBufferedWriterFor("summary");
            try {
                bufferedWriter.append(SummaryLine.summaryLine(id, elapsedTime,
                        sampleCount, startTime));
                bufferedWriter.newLine();
            } finally {
                bufferedWriter.close();
            }
        }

    }

    final static class SummaryLine {

        public static String summaryLine(final String id,
                final long elapsedTime, final long sampleCount,
                final long startTime) {
            final StringBuilder sb = new StringBuilder(id).append(":")
                    .append(longToString(elapsedTime)).append(":")
                    .append(longToString(sampleCount)).append(":")
                    .append(longToString(startTime));
            return sb.toString();
        }
    }

    @Override
    public synchronized void throughput(final long currentDuration,
            final double throughput) {
        try {
            ((BufferedWriter) throughputWriter().append(
                    ThroughputLine.throughputLine(currentDuration, throughput)))
                    .newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final static class ThroughputLine {
        public static String throughputLine(final long currentDuration,
                final double throughput) {
            return new StringBuilder(longToString(currentDuration)).append(":")
                    .append(Double.toString(throughput)).toString();
        }
    }

    @Override
    public synchronized void invocationFailed(final Throwable t) {
        try {
            failedInvocationWriter().append(t.getClass().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    private static String longToString(final long value) {
        return Long.toString(value);
    }

    private static String integerToString(final int value) {
        return Integer.toString(value);
    }

    private BufferedWriter throughputWriter() {
        return this.throughputWriter;
    }

    private BufferedWriter latencyWriter() {
        return this.latencyWriter;
    }

    private File reportDirectory() {
        return this.reportDir;
    }

    private BufferedWriter failedInvocationWriter() {
        return this.failedInvocationWriter;
    }

}
