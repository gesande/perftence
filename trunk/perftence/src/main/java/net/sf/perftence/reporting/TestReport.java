package net.sf.perftence.reporting;

public interface TestReport {

    String reportRootDirectory();

    void updateIndexFile(final String id);

    void writeSummary(final String id, final String data);
}
