package org.fluentjava.perftence.graph;

public interface GraphWriterProvider {
    GraphWriter graphWriterFor(final String id);
}
