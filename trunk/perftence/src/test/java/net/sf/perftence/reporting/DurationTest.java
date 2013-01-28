package net.sf.perftence.reporting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationTest {

    @SuppressWarnings("static-method")
    @Test
    public void hours() {
        assertEquals(3600000, Duration.hours(1));
    }

    @SuppressWarnings("static-method")
    @Test
    public void minutes() {
        assertEquals(60000, Duration.minutes(1));
    }

    @SuppressWarnings("static-method")
    @Test
    public void inMillis() {
        assertEquals(60000, Duration.millis(60000));
    }

}
