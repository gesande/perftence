package net.sf.perftence;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimerScheduler {
	private final static Logger LOG = LoggerFactory
			.getLogger(TimerScheduler.class);

	private final List<Timer> timerList;

	public TimerScheduler() {
		this.timerList = new ArrayList<>();
	}

	public void stop() {
		try {
			for (final Timer timer : timerList()) {
				timer.cancel();
			}
			log().info("All timers stopped.");
		} finally {
			timerList().clear();
		}
	}

	private static Logger log() {
		return LOG;
	}

	private List<Timer> timerList() {
		return this.timerList;
	}

	public void schedule(final TimerSpec spec) {
		addTimer(log(spec.name(), newTimer(spec)));
	}

	private void addTimer(final Timer timer) {
		timerList().add(timer);
	}

	private static Timer log(final String name, final Timer scheduled) {
		log().info("{} timer scheduled", name);
		return scheduled;
	}

	private static Timer newTimer(final TimerSpec spec) {
		final Timer timer = new Timer(spec.name());
		timer.schedule(spec.task(), spec.delay(), spec.period());
		return timer;
	}

}
