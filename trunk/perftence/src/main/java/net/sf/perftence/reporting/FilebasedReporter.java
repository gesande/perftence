package net.sf.perftence.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilebasedReporter implements InvocationReporter {

    private BufferedWriter latencyWriter;
    private BufferedWriter throughputWriter;

    public FilebasedReporter(final String id) {
        File root = new File("target", "perftence");
        File reportDir = new File(root, id);
        reportDir.mkdirs();
        try {
            this.latencyWriter = new BufferedWriter(new FileWriter(new File(
                    reportDir, "latencies")));
            this.throughputWriter = new BufferedWriter(new FileWriter(new File(
                    reportDir, "throughput")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void latency(final int latency) {
        try {
            ((BufferedWriter) this.latencyWriter.append(Integer
                    .toString(latency))).newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void summary(final String id, final long elapsedTime,
            final long sampleCount, final long startTime) {
        try {
            this.latencyWriter.close();
            this.throughputWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void throughput(final long currentDuration,
            final double throughput) {
        try {
            final StringBuilder sb = new StringBuilder(
                    Long.toString(currentDuration)).append(":").append(
                    Double.toString(throughput));
            ((BufferedWriter) this.throughputWriter.append(sb.toString()))
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
