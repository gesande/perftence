package net.sf.perftence.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilebasedReporter implements InvocationReporter {

    private BufferedWriter latencyWriter;
    private BufferedWriter throughputWriter;
    private File reportDir;

    public FilebasedReporter(final String id) {
        final File root = new File("target", "perftence");
        this.reportDir = new File(root, id);
        reportDirectory().mkdirs();
        try {
            this.latencyWriter = new BufferedWriter(new FileWriter(new File(
                    reportDirectory(), "latencies")));
            this.throughputWriter = new BufferedWriter(new FileWriter(new File(
                    reportDirectory(), "throughput")));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            final BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(new File(reportDirectory(), "summary")));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String longToString(final long elapsedTime) {
        return Long.toString(elapsedTime);
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
        return false;
    }

    @Override
    public void invocationFailed(final Throwable t) {
    }

}
