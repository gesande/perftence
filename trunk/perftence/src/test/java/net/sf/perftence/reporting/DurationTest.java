package net.sf.perftence.reporting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationTest {

	@Test
	public void hours() {
		assertEquals(3600000, Duration.hours(1));
	}

	@Test
	public void minutes() {
		assertEquals(60000, Duration.minutes(1));
	}

	@Test
	public void seconds() {
		assertEquals(1000, Duration.seconds(1));
	}

	@Test
	public void inMillis() {
		assertEquals(60000, Duration.millis(60000));
	}

}
