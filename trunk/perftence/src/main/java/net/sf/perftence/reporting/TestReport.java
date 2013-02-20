package net.sf.perftence.reporting;

public interface TestReport {
    /*
     * Root directory where report is
     */
    String directory();

    void updateIndexFile(final String id);

    void writeSummary(final String id, final String data);
}
