package net.sf.perftence.common;

import net.sf.perftence.AppendToFileFailed;

public interface FileAppendHandler {

    void failed(final String file, final AppendToFileFailed e);

    void ok(final String file);

    void start(String file);

}
