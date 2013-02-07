package net.sf.perftence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public final class PerftenceApiTest implements TestFailureNotifier {

    private Throwable failure;

    @Before
    public void before() {
        this.failure = null;
    }

    @Test
    public void fluent() {
        assertNotNull(new PerftenceApi(this).test("name"));
    }

    @Test
    public void agent() {
        assertNotNull(new PerftenceApi(this).agentBasedTest("name"));
    }

    @Test
    public void failure() {
        FailIHave t = new FailIHave();
        new PerftenceApi(this).testFailed(t);
        assertNotNull(this.failure);
        assertEquals(t, this.failure);
    }

    @Override
    public void testFailed(final Throwable t) {
        this.failure = t;
    }

    private final static class FailIHave extends Exception {//

    }
}
