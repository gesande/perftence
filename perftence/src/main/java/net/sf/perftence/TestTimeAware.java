package net.sf.perftence;

public interface TestTimeAware {
	/**
	 * Start time in milliseconds
	 */
	long startTime();

	/**
	 * Test duration in milliseconds
	 */
	long duration();

}
