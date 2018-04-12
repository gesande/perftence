package org.fluentjava.perftence.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FullyQualifiedMethodNameWithClassNameTest {

    @Test
    public void idFor() {
        assertEquals("org.fluentjava.perftence.FullyQualifiedMethodNameWithClassNameTest.id",
                new FullyQualifiedMethodNameWithClassName().idFor(this.getClass(), "id"));
    }
}
