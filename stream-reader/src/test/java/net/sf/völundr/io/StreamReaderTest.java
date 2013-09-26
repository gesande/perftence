package net.sf.völundr.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.sf.völundr.LineVisitor;

import org.junit.Test;

public class StreamReaderTest {

    @SuppressWarnings("static-method")
    @Test
    public void visit() {
        final String lines = "line1\nline2\nline3";
        final List<String> values = new ArrayList<String>();
        new StreamReader(new LineVisitor() {
            @Override
            public void visit(final String line) {
                values.add(line);
            }

            @Override
            public void emptyLine() {
                throw new FailIHave("You shouldn't come here!");
            }
        }, Charset.defaultCharset()).readFrom(toByteArrayStream(lines));

        assertEquals(3, values.size());
        assertTrue(values.contains("line1"));
        assertTrue(values.contains("line2"));
        assertTrue(values.contains("line3"));
        assertEquals("line1", values.get(0));
        assertEquals("line2", values.get(1));
        assertEquals("line3", values.get(2));
    }

    @SuppressWarnings("static-method")
    @Test
    public void visitEmptyLines() {
        final String lines = "line1\nline2\n\nline3";
        final List<String> values = new ArrayList<String>();
        new StreamReader(new LineVisitor() {
            @Override
            public void visit(final String line) {
                values.add(line);
            }

            @Override
            public void emptyLine() {
                values.add("empty line");
            }
        }, Charset.defaultCharset()).readFrom(toByteArrayStream(lines));

        assertEquals(4, values.size());
        assertTrue(values.contains("line1"));
        assertTrue(values.contains("line2"));
        assertTrue(values.contains("empty line"));
        assertTrue(values.contains("line3"));
        assertEquals("line1", values.get(0));
        assertEquals("line2", values.get(1));
        assertEquals("empty line", values.get(2));
        assertEquals("line3", values.get(3));
    }

    @SuppressWarnings("static-method")
    @Test(expected = RuntimeException.class)
    public void whenSomethingGoesWrongVisitingLine() {
        final String lines = "line1\nline2\nline3";
        new StreamReader(new LineVisitor() {

            @Override
            public void visit(final String line) {
                throw new FailIHave();
            }

            @Override
            public void emptyLine() {
                //
            }
        }, Charset.defaultCharset()).readFrom(toByteArrayStream(lines));
    }

    @SuppressWarnings("static-method")
    @Test(expected = RuntimeException.class)
    public void whenSomethingGoesWrongVisitingEmptyLine() {
        final String lines = "line1\n\nline2\nline3";
        new StreamReader(new LineVisitor() {

            @Override
            public void visit(final String line) {
                //
            }

            @Override
            public void emptyLine() {
                throw new FailIHave();

            }
        }, Charset.defaultCharset()).readFrom(toByteArrayStream(lines));
    }

    private final static class FailIHave extends RuntimeException {

        public FailIHave() {
        }

        public FailIHave(final String msg) {
            super(msg);
        }
    }

    private static InputStream toByteArrayStream(final String value) {
        return new ByteArrayInputStream(value.getBytes());
    }

}
