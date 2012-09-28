package net.sf.perftence.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.perftence.PerformanceTestSetup;

public class FilebasedReporter implements InvocationReporter {

    private BufferedWriter latencyWriter;
    private BufferedWriter throughputWriter;
    private File reportDir;
    private final boolean includeInvocationGraph;

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
            writeSetup(testSetup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter newBufferedWriterFor(String fileName)
            throws IOException {
        return new BufferedWriter(new FileWriter(new File(reportDirectory(),
                fileName)));
    }

    private void writeSetup(final PerformanceTestSetup testSetup)
            throws IOException {
        final BufferedWriter setupWriter = newBufferedWriterFor("setup");
        try {
            setupWriter.append(toString(testSetup.duration()));
            setupWriter.newLine();
            setupWriter.append(toString(testSetup.threads()));
            setupWriter.newLine();
            setupWriter.append(toString(testSetup.invocations()));
            setupWriter.newLine();
            setupWriter.append(toString(testSetup.invocationRange()));
            setupWriter.newLine();
            setupWriter.append(toString(testSetup.throughputRange()));
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
            bufferedWriter.append(id);
            bufferedWriter.newLine();
            bufferedWriter.append(longToString(elapsedTime));
            bufferedWriter.newLine();
            bufferedWriter.append(longToString(sampleCount));
            bufferedWriter.newLine();
            bufferedWriter.append(longToString(startTime));
        } finally {
            bufferedWriter.close();
        }
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
            final StringBuilder sb = new StringBuilder(
                    longToString(currentDuration)).append(":").append(
                    Double.toString(throughput));
            ((BufferedWriter) throughputWriter().append(sb.toString()))
                    .newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

    @Override
    public void invocationFailed(final Throwable t) {
    }

}
