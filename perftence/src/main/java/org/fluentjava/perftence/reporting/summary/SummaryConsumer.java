package org.fluentjava.perftence.reporting.summary;

import org.fluentjava.perftence.reporting.summary.SummaryToCsv.CsvSummary;

public interface SummaryConsumer {

    void consumeSummary(String summaryId, CsvSummary csvSummary);

    void consumeSummary(String summaryId, String summary);

}
