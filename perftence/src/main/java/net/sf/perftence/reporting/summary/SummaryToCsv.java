package net.sf.perftence.reporting.summary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class SummaryToCsv {

	public static String convertToCsv(String summary) throws IOException {
		final StringReader reader = new StringReader(summary);
		final BufferedReader br = new BufferedReader(reader);
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