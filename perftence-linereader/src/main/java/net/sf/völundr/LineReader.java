package net.sf.völundr;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public final class LineReader {
    private final Charset charset;

    public LineReader(final Charset charset) {
        this.charset = charset;
    }

    public void read(final InputStream stream, final LineVisitor visitor)
            throws IOException {
        final DataInputStream in = new DataInputStream(stream);
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    in, charSet()));
            try {
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    if (strLine.isEmpty()) {
                        visitor.emptyLine();
                    } else {
                        visitor.visit(strLine);
                    }
                }
            } finally {
                br.close();
            }
        } finally {
            in.close();
        }
    }

    private Charset charSet() {
        return this.charset;
    }
}