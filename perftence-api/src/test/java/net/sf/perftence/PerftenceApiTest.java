package net.sf.perftence;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public final class PerftenceApiTest implements TestFailureNotifier {

    @Test
    public void fluent() {
        assertNotNull(new PerftenceApi(this).test("name"));
    }

    @Test
    public void agent() {
        assertNotNull(new PerftenceApi(this).agentBasedTest("name"));
    }

    @Test
    public void requirements() {
        assertNotNull(new PerftenceApi(this).requirements());
    }

    @Test
    public void setup() {
        assertNotNull(new PerftenceApi(this).setup());
    }

    @Override
    public void testFailed(final Throwable t) {
        throw new RuntimeException(t);
    }

}
