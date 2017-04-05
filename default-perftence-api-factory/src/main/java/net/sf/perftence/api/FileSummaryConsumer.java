package net.sf.perftence.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
			FileUtil.appendToFile(path + "/" + summaryId, ToBytes
					.withDefaultCharset().convert(convertToCsv(summary)));
		} catch (AppendToFileFailed | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String convertToCsv(String summary) throws IOException {
		StringReader reader = new StringReader(summary);
		BufferedReader br = new BufferedReader(reader);
		try {
			StringBuilder cols = new StringBuilder();
			StringBuilder rows = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(":");
				String field = values[0].replaceAll(" ", "_");
				String value = values[1].trim();
				cols.append(field + ",");
				rows.append(value + ",");
			}
			String string = cols.toString();
			String string2 = rows.toString();
			return string.substring(0, string.length() - 1) + "\n"
					+ string2.substring(0, string2.length() - 1);
		} finally {
			br.close();
			reader.close();
		}
	}
}