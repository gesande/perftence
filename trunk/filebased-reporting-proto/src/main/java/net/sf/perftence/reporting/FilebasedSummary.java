package net.sf.perftence.reporting;

public class FilebasedSummary {

    private long elapsedTime;
    private long sampleCount;
    private long startTime;

    public FilebasedSummary(final long elapsedTime, final long sampleCount,
            final long startTime) {
        this.elapsedTime = elapsedTime;
        this.sampleCount = sampleCount;
        this.startTime = startTime;
    }

    public long elapsedTime() {
        return this.elapsedTime;
    }

    public long sampleCount() {
        return this.sampleCount;
    }

    public long startTime() {
        return this.startTime;
    }

}
