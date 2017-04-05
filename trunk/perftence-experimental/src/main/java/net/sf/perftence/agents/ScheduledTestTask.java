package net.sf.perftence.agents;

import java.util.concurrent.TimeUnit;

/**
 * Add to a TestTask a scheduling nature.
 * 
 * ALWAYS REMEMBER TO CREATE THE hashcode() -METHOD FROM SCRATCH IF YOU ADD SOME
 * NEW FIELDS!
 * 
 */
final class ScheduledTestTask implements Comparable<ScheduledTestTask> {

	private final TestTask task;
	private final long scheduleTime;
	private final long scheduledRunTimeInNanos;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return this.compareTo((ScheduledTestTask) obj) == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.scheduleTime ^ (this.scheduleTime >>> 32));
		result = prime * result + (int) (this.scheduledRunTimeInNanos
				^ (this.scheduledRunTimeInNanos >>> 32));
		result = prime * result
				+ ((this.task == null) ? 0 : this.task.hashCode());
		return result;
	}

	public ScheduledTestTask(final TestTask task, final long scheduleTime) {
		this.task = task;
		this.scheduleTime = scheduleTime;
		this.scheduledRunTimeInNanos = TimeSpecificationFactory
				.toNanos(task.when()) + scheduleTime;
	}

	TestTask task() {
		return this.task;
	}

	/**
	 * 
	 * @param now
	 *            in nanos (usually from System.nanoTime() )
	 * @return true indicating it's time to run the task
	 */
	boolean isTimeToRun(final long now) {
		return isTimeToRun(task().when(), now);
	}

	private boolean isTimeToRun(final Time time, final long now) {
		return (time.time() == 0) ? true : scheduledRunTimeInNanos() <= now;
	}

	private long scheduledRunTimeInNanos() {
		return this.scheduledRunTimeInNanos;
	}

	/**
	 * 
	 * @return The time is was supposed to be run, in nanos.
	 */
	long timeItWasScheduledToBeRun() {
		return scheduledRunTimeInNanos();
	}

	private Long scheduleTime() {
		return this.scheduleTime;
	}

	@Override
	public int compareTo(final ScheduledTestTask o) {
		if (o == null)
			return 1;
		final int compare = compare(this.task(), o.task());
		return compare == 0 ? this.scheduleTime().compareTo(o.scheduleTime())
				: compare;
	}

	private static int compare(final TestTask o1, final TestTask o2) {
		final Time when = o1.when();
		final Time when2 = o2.when();
		final Long time1 = TimeUnit.NANOSECONDS.convert(when.time(),
				when.timeUnit());
		final Long time2 = TimeUnit.NANOSECONDS.convert(when2.time(),
				when2.timeUnit());
		return time1.compareTo(time2);
	}
}