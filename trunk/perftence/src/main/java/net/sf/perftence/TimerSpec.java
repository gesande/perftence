package net.sf.perftence;

import java.util.TimerTask;

/**
 * A specification of a timely executable task (see {@link TimerTask}) to be
 * executed in the background, i.e. in a separate thread.
 * 
 */
public interface TimerSpec {

	/**
	 * Name of the task, i.e. thread name running the task, see
	 * {@link TimerTask}
	 */
	String name();

	/**
	 * Delay for starting the task, see {@link TimerTask}
	 */
	int delay();

	/**
	 * Interval for executing the task, see {@link TimerTask}
	 */
	int period();

	/**
	 * Task to be executed, see {@link TimerTask}
	 */
	TimerTask task();
}