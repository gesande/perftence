package net.sf.perftence.api;

import net.sf.perftence.reporting.summary.SummaryConsumer;
import net.sf.perftence.reporting.summary.SummaryToCsv.CsvSummary;
import net.sf.völundr.fileio.FileUtil;
import net.sf.völundr.fileio.ToBytes;
import net.sf.völundr.fileio.WritingFileFailed;

public final class SummaryToFileWriter implements SummaryConsumer {

    private final String path;

    public SummaryToFileWriter(String path) {
        this.path = path;
    }

    @Override
    public void consumeSummary(String summaryId, CsvSummary csvSummary) {
        consumeSummary(summaryId, csvSummary.toString());
    }

    @Override
    public void consumeSummary(String summaryId, String summary) {
        try {
            FileUtil.writeToFile(this.path + "/" + summaryId, ToBytes.withDefaultCharset().convert(summary));
        } catch (WritingFileFailed e) {
            throw new RuntimeException(e);
        }
    }

}