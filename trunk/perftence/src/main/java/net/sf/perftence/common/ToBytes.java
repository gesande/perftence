package net.sf.perftence.common;

import java.nio.charset.Charset;

final class ToBytes {
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
}