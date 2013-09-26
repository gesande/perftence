package net.sf.perftence.common;

import net.sf.völundr.fileio.AppendToFileFailed;

public interface FileAppendHandler {

    void failed(final String file, final AppendToFileFailed e);

    void ok(final String file);

    void start(String file);

}
