package net.sf.perftence.agents;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class TaskProvider implements TaskSchedulingStatisticsProvider {

	private final static Logger LOG = LoggerFactory
			.getLogger(TaskProvider.class);

	private final long waitTimeout;
	private final SortedSet<ScheduledTestTask> scheduledTasks;

	/**
	 * @param time
	 *            The maximum time to wait.
	 */
	public TaskProvider(final Time time) {
		this.waitTimeout = TimeUnit.MILLISECONDS.convert(time.time(),
				time.timeUnit());
		this.scheduledTasks = new TreeSet<ScheduledTestTask>();
	}

	public ScheduledTestTask waitForNextScheduledTask()
			throws InterruptedException {
		synchronized (this.scheduledTasks) {
			ScheduledTestTask first;
			while (true) {
				first = nextScheduledTask();
				if (isTimeToRun(first)) {
					break;
				}
				this.scheduledTasks.wait(waitTimeout());
			}
			boolean remove = this.scheduledTasks.remove(first);
			if (!remove) {
				log().warn(
						"Unable to remove the task from the scheduled tasks!");
			}
			// VERY BIG IF: if workers not active enough: check if there are
			// more tasks to run and notify
			if (isTimeToRun(nextScheduledTask())) {
				this.scheduledTasks.notifyAll();
			}
			return first;
		}
	}

	private ScheduledTestTask nextScheduledTask() {
		return this.scheduledTasks.isEmpty() ? null : this.scheduledTasks
				.first();
	}

	private static boolean isTimeToRun(final ScheduledTestTask first) {
		return first != null && first.isTimeToRun(System.nanoTime());
	}

	private long waitTimeout() {
		return this.waitTimeout;
	}

	public void schedule(final TestTask task) throws ScheduleFailedException {
		synchronized (this.scheduledTasks) {
			final ScheduledTestTask scheduled = new ScheduledTestTask(task,
					System.nanoTime());
			if (!this.scheduledTasks.add(scheduled)) {
				throw new ScheduleFailedException(
						"Something weird happened, the new scheduled task was already in the scheduled list!");
			}
			if (isTimeToRun(nextScheduledTask())) {
				this.scheduledTasks.notifyAll();
			}
		}
	}

	@Override
	public int scheduledTasks() {
		synchronized (this.scheduledTasks) {
			return this.scheduledTasks.size();
		}
	}

	public boolean hasScheduledTasks() {
		synchronized (this.scheduledTasks) {
			return !this.scheduledTasks.isEmpty();
		}
	}

	private static Logger log() {
		return LOG;
	}

	@Override
	public Time lastTaskScheduledToBeRun() {
		return hasScheduledTasks() ? TimeSpecificationFactory
				.inNanos(this.scheduledTasks.last().timeItWasScheduledToBeRun())
				: TimeSpecificationFactory.inMillis(-1);
	}
}