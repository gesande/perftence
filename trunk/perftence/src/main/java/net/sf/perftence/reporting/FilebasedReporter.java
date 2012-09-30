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

    public FilebasedReporter(final String id,
            final boolean includeInvocationGraph,
            final PerformanceTestSetup testSetup) {
        this.includeInvocationGraph = includeInvocationGraph;
        final File root = new File("target", "perftence");
        this.reportDir = new File(root, id);
        reportDirectory().mkdirs();
        try {
            this.latencyWriter = newBufferedWriterFor("latencies");
            this.throughputWriter = newBufferedWriterFor("throughput");
            this.failedInvocationWriter = newBufferedWriterFor("failed-invocations");
            writeSetup(testSetup, includeInvocationGraph);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter newBufferedWriterFor(final String fileName)
            throws IOException {
        return new BufferedWriter(new FileWriter(new File(reportDirectory(),
                fileName)));
    }

    private void writeSetup(final PerformanceTestSetup testSetup,
            final boolean includeInvocationGraph) throws IOException {
        final BufferedWriter setupWriter = newBufferedWriterFor("setup");
        try {
            setupWriter.append(toString(testSetup.duration()));
            setupWriter.append(":");
            setupWriter.append(toString(testSetup.threads()));
            setupWriter.append(":");
            setupWriter.append(toString(testSetup.invocations()));
            setupWriter.append(":");
            setupWriter.append(toString(testSetup.invocationRange()));
            setupWriter.append(":");
            setupWriter.append(toString(testSetup.throughputRange()));
            setupWriter.append(":");
            setupWriter.append(Boolean.toString(includeInvocationGraph));
        } finally {
            setupWriter.close();
        }
    }

    @SuppressWarnings("static-method")
    private String toString(final int value) {
        return Integer.toString(value);
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
            writeSummary(id, elapsedTime, sampleCount, startTime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSummary(final String id, final long elapsedTime,
            final long sampleCount, final long startTime) throws IOException {
        final BufferedWriter bufferedWriter = newBufferedWriterFor("summary");
        try {
            bufferedWriter.append(summaryLine(id, elapsedTime, sampleCount,
                    startTime));
            bufferedWriter.newLine();
        } finally {
            bufferedWriter.close();
        }
    }

    private static String summaryLine(final String id, final long elapsedTime,
            final long sampleCount, final long startTime) {
        final StringBuilder sb = new StringBuilder(id).append(":")
                .append(longToString(elapsedTime)).append(":")
                .append(longToString(sampleCount)).append(":")
                .append(longToString(startTime));
        return sb.toString();
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
        return this.reportDir;
    }

    @Override
    public synchronized void throughput(final long currentDuration,
            final double throughput) {
        try {
            ((BufferedWriter) throughputWriter().append(
                    currentThroughput(currentDuration, throughput))).newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String currentThroughput(final long currentDuration,
            final double throughput) {
        return new StringBuilder(longToString(currentDuration)).append(":")
                .append(Double.toString(throughput)).toString();
    }

    @Override
    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    @Override
    public synchronized void invocationFailed(final Throwable t) {
        try {
            this.failedInvocationWriter.append(t.getClass().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
