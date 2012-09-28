package net.sf.perftence;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("static-method")
public class LineReader {
    public void read(final InputStream stream, final LineVisitor visitor)
            throws IOException {
        final DataInputStream in = new DataInputStream(stream);
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    in));
            try {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    if (!strLine.isEmpty()) {
                        visitor.visit(strLine);
                    } else {
                        visitor.emptyLine();
                    }
                }
            } finally {
                br.close();
            }
        } finally {
            in.close();
        }
    }
}