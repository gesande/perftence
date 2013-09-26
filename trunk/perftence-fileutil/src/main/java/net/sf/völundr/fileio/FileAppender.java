package net.sf.völundr.fileio;

final public class FileAppender {
    private final FileAppendHandler handler;
    private final ToBytes toBytes;

    public FileAppender(final ToBytes toBytes, final FileAppendHandler handler) {
        this.toBytes = toBytes;
        this.handler = handler;
    }

    public void appendToFile(final String file, final String data) {
        handler().start(file);
        try {
            FileUtil.appendToFile(file, toBytes(data));
            handler().ok(file);
        } catch (final AppendToFileFailed e) {
            handler().failed(file, e);
        }
    }

    private byte[] toBytes(final String data) {
        return this.toBytes.convert(data);
    }

    private FileAppendHandler handler() {
        return this.handler;
    }
}