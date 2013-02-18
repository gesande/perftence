package net.sf.perftence.reporting.graph;

public interface GraphWriterProvider {
    GraphWriter graphWriterFor(final String id);
}
