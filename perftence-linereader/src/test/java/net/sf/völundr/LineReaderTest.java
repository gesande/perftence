package net.sf.völundr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class LineReaderTest {

    @SuppressWarnings("static-method")
    @Test
    public void noEmptyLines() throws IOException {
        final InputStream stream = inputStreamWith3Lines();
        final AtomicBoolean emptyLineDetected = new AtomicBoolean(false);
        final List<String> lines = new ArrayList<String>(3);
        new LineReader(Charset.defaultCharset()).read(stream,
                new LineVisitor() {

                    @Override
                    public void visit(final String line) {
                        lines.add(line);
                    }

                    @Override
                    public void emptyLine() {
                        emptyLineDetected.getAndSet(true);
                    }
                });
        assertEquals(3, lines.size());
        assertFalse(emptyLineDetected.get());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
        assertEquals("line3", lines.get(2));
    }

    @SuppressWarnings("static-method")
    @Test
    public void emptyLines() throws IOException {
        final InputStream byteArrayInputStream = contentsWithEmptyLine();
        final AtomicBoolean emptyLineDetected = new AtomicBoolean(false);
        final List<String> lines = new ArrayList<String>(3);
        new LineReader(Charset.defaultCharset()).read(byteArrayInputStream,
                new LineVisitor() {

                    @Override
                    public void visit(final String line) {
                        lines.add(line);
                    }

                    @Override
                    public void emptyLine() {
                        lines.add("");
                        emptyLineDetected.getAndSet(true);
                    }
                });
        assertEquals(3, lines.size());
        assertTrue(emptyLineDetected.get());
        assertEquals("line1", lines.get(0));
        assertEquals("", lines.get(1));
        assertEquals("line3", lines.get(2));
    }

    @SuppressWarnings("static-method")
    @Test(expected = FailIHave.class)
    public void exceptionDuringVisit() throws IOException {
        new LineReader(Charset.defaultCharset()).read(inputStreamWith3Lines(),
                new LineVisitor() {

                    @Override
                    public void visit(final String line) {
                        throw new FailIHave();
                    }

                    @Override
                    public void emptyLine() {//
                    }
                });
    }

    @SuppressWarnings("static-method")
    @Test(expected = FailIHave.class)
    public void exceptionDuringEmptyLine() throws IOException {
        new LineReader(Charset.defaultCharset()).read(contentsWithEmptyLine(),
                new LineVisitor() {

                    @Override
                    public void visit(final String line) {//
                    }

                    @Override
                    public void emptyLine() {
                        throw new FailIHave();
                    }
                });
    }

    private final static class FailIHave extends RuntimeException {//

    }

    private static InputStream contentsWithEmptyLine() {
        return newByteArrayInputStream(new StringBuilder().append("line1\n")
                .append("\n").append("line3"));
    }

    private static InputStream inputStreamWith3Lines() {
        return newByteArrayInputStream(new StringBuilder().append("line1\n")
                .append("line2\n").append("line3\n"));
    }

    private static InputStream newByteArrayInputStream(final StringBuilder sb) {
        return new ByteArrayInputStream(sb.toString().getBytes(
                Charset.defaultCharset()));
    }
}
