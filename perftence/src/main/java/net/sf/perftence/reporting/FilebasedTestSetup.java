package net.sf.perftence.reporting;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectStreamException;
import java.io.Serializable;

import net.sf.perftence.PerformanceTestSetup;

public class FilebasedTestSetup implements Serializable {
    private PerformanceTestSetup testSetup;
    private boolean includeInvocationGraph;

    public FilebasedTestSetup(final PerformanceTestSetup testSetup,
            final boolean includeInvocationGraph) {
        this.testSetup = testSetup;
        this.includeInvocationGraph = includeInvocationGraph;
    }

    private void writeObject(final java.io.ObjectOutputStream out)
            throws IOException {
        out.putFields().put("testSetup", testSetup());
        out.putFields().put("includeInvocationGraph", includeInvocationGraph());
        out.writeFields();
    }

    private void readObject(final java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        GetField readFields = in.readFields();
        this.testSetup = (PerformanceTestSetup) readFields.get("testSetup",
                null);
        this.includeInvocationGraph = readFields.get("includeInvocationGraph",
                true);
    }

    @SuppressWarnings("unused")
    private void readObjectNoData() throws ObjectStreamException {
        // no need for this
    }

    public PerformanceTestSetup testSetup() {
        return this.testSetup;
    }

    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }

}
