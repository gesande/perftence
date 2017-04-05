package net.sf.perftence;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.reporting.Duration;
import net.sf.perftence.reporting.summary.Summary;
import net.sf.perftence.reporting.summary.SummaryAppender;

/**
 * First extend from AbstractMultiThreadedTest to get access to test entry
 * points decorate with @RunWith using DefaultTestRunner (or or your test runner
 * extending AbstractTestRunner to get the FailureNotifier injected properly)
 * 
 * For example like this:
 */
@RunWith(DefaultTestRunner.class)
public final class FluentBasedTestExample extends AbstractMultiThreadedTest {

	private final static Logger LOG = LoggerFactory
			.getLogger(FluentBasedTestExample.class);
	private final static Random RANDOM = new Random(System.currentTimeMillis());

	@Test
	public void invocationBased() throws Exception {
		test().setup(setup().invocations(100).invocationRange(10).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						sleep(10);
					}
				}).start();
	}

	@Test
	public void threadBased() throws Exception {
		test().setup(setup().threads(100).invocations(5000).invocationRange(20)
				.build()).executable(new Executable() {
					@Override
					public void execute() throws Exception {
						sleep(10);
					}
				}).start();
	}

	@Test
	public void durationBasedSingleThread() throws Exception {
		test().setup(setup().duration(Duration.seconds(5)).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						sleep(100);
					}
				}).start();
	}

	@Test
	public void durationBasedMultiThread() throws Exception {
		test().setup(setup().duration(Duration.seconds(2)).threads(2)
				.invocationRange(20).throughputRange(2000000).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						sleep(100);
					}
				}).start();
	}

	@Test
	public void performanceRequirements() throws Exception {
		test().setup(setup().threads(10).invocations(100).build())
				.requirements(
						requirements().average(30).max(200).median(20).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						sleep(20);
					}
				}).start();
	}

	@Test
	public void durationWithRanges() throws Exception {
		test().setup(setup().duration(Duration.seconds(5)).threads(10)
				.invocationRange(100).throughputRange(500).build())
				.executable(new Executable() {

					@Override
					public void execute() throws Exception {
						sleep(random(100) + 1);
					}

				}).start();
	}

	@Test
	public void customSummaryAppender() throws Exception {
		final SummaryAppender summaryAppender = new SummaryAppender() {
			@Override
			public void append(Summary<?> summary) {
				summary.text("Here's something cool!").endOfLine()
						.bold("And some bolded text").endOfLine();
			}
		};
		test().setup(setup().duration(Duration.seconds(15)).threads(10)
				.invocationRange(1000).throughputRange(30)
				.summaryAppender(summaryAppender).build())
				.executable(new Executable() {

					@Override
					public void execute() throws Exception {
						randomSleep(1000);
					}

				}).start();
	}

	@Test
	public void noInvocationGraph() throws Exception {
		test().noInvocationGraph()
				.setup(setup().duration(Duration.seconds(5)).threads(10)
						.invocationRange(1000).throughputRange(30).build())
				.executable(new Executable() {

					@Override
					public void execute() throws Exception {
						randomSleep(1000);
					}
				}).start();
	}

	@Test
	public void threadBasedWithAllowedException() throws Exception {
		final ExecutorBehavingBadlyHalfTheTime failingExecutor = new ExecutorBehavingBadlyHalfTheTime();
		test().setup(setup().threads(10).invocations(100).throughputRange(50)
				.build()).allow(MyException.class).executable(new Executable() {
					@Override
					public void execute() throws Exception {
						failingExecutor.execute();
					}
				}).start();
	}

	class ExecutorBehavingBadlyHalfTheTime implements Executable {
		final AtomicInteger counter = new AtomicInteger();

		@Override
		public void execute() throws Exception {
			if (this.counter.get() % 2 == 0) {
				this.counter.incrementAndGet();
				throw new MyException(
						"null pointer executing NullPointerExceptionExecutor");
			}
			this.counter.incrementAndGet();
			Thread.sleep(500);
		}

	}

	class MyException extends Exception {

		public MyException(final String msg) {
			super(msg);
		}
	}

	private static int random(final int n) {
		return RANDOM.nextInt(n);
	}

	private static void randomSleep(final int max) throws InterruptedException {
		sleep(random(max));
	}

	private static void sleep(final int value) throws InterruptedException {
		Thread.sleep(value);
	}

	private static Logger log() {
		return LOG;
	}

	@SuppressWarnings("static-method")
	@After
	public void after() {
		log().info("Its over...finally...phuuiiii, i'm done.");
	}
}
