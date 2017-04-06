package net.sf.perftence.fluent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;
import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;
import net.sf.perftence.reporting.Duration;

@RunWith(DefaultTestRunner.class)
public class FluentUserStories extends AbstractMultiThreadedTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(FluentUserStories.class);
	private Executable executor;
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private int i;

	private final Executable executor() {
		return this.executor;
	}

	@Before
	public void before() throws Exception {
		this.i = 0;
		this.executor = () -> {
			FluentUserStories.this.i++;
			if (FluentUserStories.this.i % 1000 == 0) {
				log().info("Executing...{}", FluentUserStories.this.i);
			}
		};
	}

	@Test
	public void learning() {
		final AtomicInteger i = new AtomicInteger(0);
		for (int index = 0; index < 100; index++) {
			log().info("{} % 10 == 0 -> {}", i.intValue(),
					i.intValue() % 10 == 0);
			i.incrementAndGet();
		}

	}

	@After
	public void after() {
		log().info("Its over...finally...phuuiiii, i'm done.");
	}

	private static Logger log() {
		return LOG;
	}

	@Test
	public void invocationCount() throws Exception {
		final AtomicInteger i = new AtomicInteger(0);
		test().setup(setup().threads(3).invocations(10).build())
				.executable(() -> {
					executor().execute();
					i.incrementAndGet();
				}).start();
		Assert.assertEquals("Invocation count doesn't match!", 10, i.get());
	}

	@Test
	public void durationMultiThreadSleepingMoreThanOneSecondExecutable()
			throws Exception {
		test().setup(setup().duration(Duration.seconds(15)).threads(10)
				.invocationRange(1000).throughputRange(30).build())
				.executable(() -> Thread.sleep(RANDOM.nextInt(1000))).start();
	}

	@Test
	public void customSummaryAppender() throws Exception {
		test().setup(setup().duration(Duration.seconds(15)).threads(10)
				.invocationRange(1000).throughputRange(30)
				.summaryAppender(summary -> summary
						.text("Here's something cool!").endOfLine()
						.bold("And some bolded text").endOfLine())
				.build()).executable(() -> Thread.sleep(RANDOM.nextInt(1000)))
				.start();
	}
}
