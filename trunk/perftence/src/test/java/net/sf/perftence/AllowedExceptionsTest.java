package net.sf.perftence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllowedExceptionsTest {

    @Test
    public void test() {
        AllowedExceptions ae = new AllowedExceptions();
        MyException exception = new MyException();
        assertFalse(ae.isAllowed(exception));
        ae.allow(MyException.class);
        assertTrue(ae.isAllowed(exception));
    }

    final static class MyException extends Exception {
        public MyException() {
            super();
        }
    }
}
