package net.sf.perftence;

import static org.junit.Assert.assertNotNull;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;

import org.junit.Test;

public final class PerftenceApiTest implements TestFailureNotifier {

    @Test
    public void fluent() {
        assertNotNull(perftenceApi().test("name"));
    }

    @Test
    public void agent() {
        assertNotNull(perftenceApi().agentBasedTest("name"));
    }

    @Test
    public void requirements() {
        assertNotNull(perftenceApi().requirements());
    }

    @Test
    public void setup() {
        assertNotNull(perftenceApi().setup());
    }

    @Override
    public void testFailed(final Throwable t) {
        throw new RuntimeException(t);
    }

    private PerftenceApi perftenceApi() {
        return new PerftenceApi(this, new DefaultTestRuntimeReporterFactory());
    }

}
