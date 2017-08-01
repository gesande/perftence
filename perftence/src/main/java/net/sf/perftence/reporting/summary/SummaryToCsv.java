package net.sf.perftence.reporting.summary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.sf.perftence.PerftenceRuntimeException;

public class SummaryToCsv {

    public static CsvSummary convertToCsv(String summary) {
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
            return new CsvSummary(string.substring(0, string.length() - 1), string2.substring(0, string2.length() - 1));
        } catch (IOException e) {
            throw new PerftenceRuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new PerftenceRuntimeException(e);
            }
            reader.close();
        }
    }

    public static final class CsvSummary {

        private final String columnRow;
        private final String valueRow;

        private CsvSummary(String columnRow, String valueRow) {
            this.columnRow = columnRow;
            this.valueRow = valueRow;
        }

        @Override
        public String toString() {
            return this.columnRow + "\n" + this.valueRow;
        }

        public String columnRow() {
            return this.columnRow;
        }

        public String valueRow() {
            return this.valueRow;
        }
    }
}
