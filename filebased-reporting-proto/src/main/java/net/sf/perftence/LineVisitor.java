package net.sf.perftence;

public interface LineVisitor {
    void visit(final String line);

    void emptyLine();
}