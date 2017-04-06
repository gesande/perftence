package net.sf.perftence.api;

import java.io.IOException;

import net.sf.perftence.common.SummaryConsumer;
import net.sf.perftence.reporting.summary.SummaryToCsv;
import net.sf.völundr.fileio.FileUtil;
import net.sf.völundr.fileio.ToBytes;
import net.sf.völundr.fileio.WritingFileFailed;

public final class SummaryToCsvFile implements SummaryConsumer {

	private String path;

	public SummaryToCsvFile(String path) {
		this.path = path;
	}

	@Override
	public void consumeSummary(String summaryId, String summary) {
		try {
			FileUtil.writeToFile(path + "/" + summaryId,
					ToBytes.withDefaultCharset()
							.convert(SummaryToCsv.convertToCsv(summary)));
		} catch (WritingFileFailed | IOException e) {
			throw new RuntimeException(e);
		}
	}

}