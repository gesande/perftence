package net.sf.perftence.agents;

import java.util.concurrent.TimeUnit;

public interface Time {

	/**
	 * Time from now
	 */
	long time();

	/**
	 * Time unit for the time
	 */
	TimeUnit timeUnit();

}