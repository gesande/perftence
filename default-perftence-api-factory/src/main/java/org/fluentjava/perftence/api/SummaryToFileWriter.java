package org.fluentjava.perftence.api;

import org.fluentjava.perftence.PerftenceRuntimeException;
import org.fluentjava.perftence.reporting.summary.SummaryConsumer;
import org.fluentjava.perftence.reporting.summary.SummaryToCsv.CsvSummary;

import net.sf.völundr.fileio.FileUtil;
import net.sf.völundr.fileio.WritingFileFailed;
import net.sf.völundr.io.StringToBytes;

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
            FileUtil.writeToFile(this.path + "/" + summaryId, StringToBytes.withDefaultCharset().convert(summary));
        } catch (WritingFileFailed e) {
            throw new PerftenceRuntimeException(e);
        }
    }

}