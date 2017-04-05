package net.sf.perftence.api;

import net.sf.perftence.common.SummaryConsumer;
import net.sf.völundr.fileio.AppendToFileFailed;
import net.sf.völundr.fileio.FileUtil;
import net.sf.völundr.fileio.ToBytes;

public final class FileSummaryConsumer implements SummaryConsumer {
	private String path;

	public FileSummaryConsumer(String path) {
		this.path = path;
	}

	@Override
	public void consumeSummary(String summaryId, String summary) {
		try {
			FileUtil.appendToFile(path + "/" + summaryId,
					ToBytes.withDefaultCharset().convert(summary));
		} catch (AppendToFileFailed e) {
			throw new RuntimeException(e);
		}
	}
}