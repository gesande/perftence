package net.sf.perftence.reporting;

import net.sf.perftence.PerformanceTestSetup;

public class FilebasedTestSetup {
    private final PerformanceTestSetup testSetup;
    private final boolean includeInvocationGraph;

    public FilebasedTestSetup(final PerformanceTestSetup testSetup,
            final boolean includeInvocationGraph) {
        this.testSetup = testSetup;
        this.includeInvocationGraph = includeInvocationGraph;
    }

    public PerformanceTestSetup testSetup() {
        return this.testSetup;
    }

    public boolean includeInvocationGraph() {
        return this.includeInvocationGraph;
    }
}
