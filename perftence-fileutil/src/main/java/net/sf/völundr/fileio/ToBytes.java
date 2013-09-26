package net.sf.völundr.fileio;

import java.nio.charset.Charset;

public final class ToBytes {
    private final Charset charset;

    public ToBytes(final Charset charset) {
        this.charset = charset;
    }

    public byte[] convert(final String data) {
        return data.getBytes(charset());
    }

    private Charset charset() {
        return this.charset;
    }

    public static ToBytes withDefaultCharset() {
        return new ToBytes(Charset.defaultCharset());
    }
}