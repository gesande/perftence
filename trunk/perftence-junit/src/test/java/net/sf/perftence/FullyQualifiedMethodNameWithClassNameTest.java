package net.sf.perftence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FullyQualifiedMethodNameWithClassNameTest {

    @Test
    public void idFor() {
        assertEquals("net.sf.perftence.FullyQualifiedMethodNameWithClassNameTest.id",
                new FullyQualifiedMethodNameWithClassName().idFor(this.getClass(), "id"));
    }
}
