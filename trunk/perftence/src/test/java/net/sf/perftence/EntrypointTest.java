package net.sf.perftence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class EntrypointTest extends AbstractMultiThreadedTest {

    @Test
    public void checkSetup() {
        assertNotNull(setup());
    }

    @Test
    public void checkRequirements() {
        assertNotNull(requirements());
    }

    @Test
    public void checkAgent() {
        assertNotNull(agentBasedTest());
    }

    @Test
    public void checkNamedAgent() {
        assertNotNull(agentBasedTest("name"));
    }

    @Test
    public void checkFluent() {
        assertNotNull(test());
    }

    @Test
    public void checkNamedFluent() {
        assertNotNull(test("name"));
    }

    @Test
    public void checkFailureNotifier() {
        assertNotNull(failureNotifier());
    }

    @Test
    public void checkTestMethodName() {
        assertEquals("checkTestMethodName", testMethodName());
    }

    @Test
    public void checkId() {
        assertEquals("net.sf.perftence.EntrypointTest.checkId", id());
    }

    @Test
    public void checkFullyQualifiedMethodNameWithClassName() {
        assertEquals(
                "net.sf.perftence.EntrypointTest.checkFullyQualifiedMethodNameWithClassName",
                fullyQualifiedMethodNameWithClassName());
    }
    
}
