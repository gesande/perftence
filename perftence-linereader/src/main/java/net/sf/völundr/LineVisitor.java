package net.sf.völundr;

public interface LineVisitor {
    void visit(final String line);

    void emptyLine();
}