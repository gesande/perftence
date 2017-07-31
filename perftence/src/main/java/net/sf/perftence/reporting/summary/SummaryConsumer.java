package net.sf.perftence.reporting.summary;

import net.sf.perftence.reporting.summary.SummaryToCsv.CsvSummary;

public interface SummaryConsumer {

    void consumeSummary(String summaryId, CsvSummary csvSummary);

    void consumeSummary(String summaryId, String summary);

}
