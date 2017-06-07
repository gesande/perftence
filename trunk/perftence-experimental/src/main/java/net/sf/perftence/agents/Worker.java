package net.sf.perftence.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.TestFailureNotifier;

final class Worker implements Runnable {
	private final static Logger LOG = LoggerFactory.getLogger(Worker.class);
	private final TaskProvider taskProvider;
	private final RunnableAdapter runnableProvider;
	private final TestFailureNotifier testFailureNotifier;
	private WorkerState state;

	public Worker(final TaskProvider taskProvider,
			final RunnableAdapter runnableProvider,
			final TestFailureNotifier testFailureNotifier) {
		this.taskProvider = taskProvider;
		this.runnableProvider = runnableProvider;
		this.testFailureNotifier = testFailureNotifier;
		enterState(WorkerState.Initialized);
	}

	enum WorkerState {
		Waiting {
			@Override
			public boolean isBusy() {
				return false;
			}
		},
		Running {
			@Override
			public boolean isBusy() {
				return true;
			}
		},
		Finished {
			@Override
			public boolean isBusy() {
				return false;
			}
		},
		Interrupted {
			@Override
			public boolean isBusy() {
				return false;
			}
		},
		Initialized {
			@Override
			public boolean isBusy() {
				return false;
			}
		};

		public abstract boolean isBusy();

	}

	public synchronized boolean isBusy() {
		return state().isBusy();
	}

	@Override
	public void run() {
		while (true) {
			try {
				enterState(WorkerState.Waiting);
				final ScheduledTestTask task = taskProvider()
						.waitForNextScheduledTask();
				if (task == null) {
					enterState(WorkerState.Finished);
					log().debug("Worker finished.");
					return;
				}
				enterState(WorkerState.Running);
				run(task);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				handleInterruption();
				break;
			} catch (Throwable cause) {
				handleFailure(cause);
			}
		}
	}

	private void handleInterruption() {
		enterState(WorkerState.Interrupted);
		log().debug("Worker interrupted, time to stop working and go home.");
	}

	private void handleFailure(final Throwable cause) {
		log().error("Error running a test task!", cause);
		failureNotifier().testFailed(cause);
	}

	private void run(final ScheduledTestTask task)
			throws ScheduleFailedException {
		runAndMeasure(task);
		scheduleNextTask(task.task());
	}

	private void runAndMeasure(final ScheduledTestTask scheduled) {
		run(scheduled.task(), scheduled.timeItWasScheduledToBeRun());
	}

	private void run(final TestTask task, final long timeItWasScheduled) {
		runnableProvider().adapt(task, timeItWasScheduled).run();
	}

	private void scheduleNextTask(final TestTask task)
			throws ScheduleFailedException {
		final TestTask maybeNext = task.nextTaskIfAny();
		// joko näin:
		if (maybeNext != null) {
			taskProvider().schedule(maybeNext);
		}
		// tai jos perffi vaatii, aja suoraan itse
	}

	private RunnableAdapter runnableProvider() {
		return this.runnableProvider;
	}

	private synchronized void enterState(final WorkerState newState) {
		this.state = newState;
	}

	private TaskProvider taskProvider() {
		return this.taskProvider;
	}

	private WorkerState state() {
		return this.state;
	}

	private TestFailureNotifier failureNotifier() {
		return this.testFailureNotifier;
	}

	private static Logger log() {
		return LOG;
	}

}