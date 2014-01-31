package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;
import net.sf.perftence.agents.TestAgent;
import net.sf.perftence.agents.TestTask;
import net.sf.perftence.agents.TestTaskCategory;
import net.sf.perftence.agents.TestTaskReporter;
import net.sf.perftence.agents.Time;
import net.sf.perftence.agents.TimeSpecificationFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class ProduceGood extends AbstractMultiThreadedTest {

	@Test
	public void graphsForFluentBased() {
		final int invocations = 50000;
		final Stack<Integer> stack = new Stack<Integer>();
		setupStack(stack);
		test().setup(
				setup().threads(400).invocations(invocations)
						.invocationRange(110).throughputRange(10000).build())
				.executable(new Executable() {

					@Override
					public void execute() throws Exception {
						Thread.sleep(stack.pop());
					}
				}).start();
	}

	@Test
	public void graphsForAgentBased() {
		final Stack<Integer> stack = new Stack<Integer>();
		setupStack(stack);
		agentBasedTest().latencyGraphForAll().agents(agents(50000, stack))
				.workers(400).start();
	}

	enum Category implements TestTaskCategory {
		Sleep;
	}

	private static Collection<TestAgent> agents(final int size,
			final Stack<Integer> stack) {
		List<TestAgent> list = new ArrayList<TestAgent>();
		for (int i = 0; i < size; i++) {
			list.add(new TestAgent() {

				@Override
				public TestTask firstTask() {
					return newTask(stack);
				}

				private TestTask newTask(final Stack<Integer> stack) {
					return new TestTask() {

						@Override
						public Time when() {
							return TimeSpecificationFactory.now();
						}

						@Override
						public void run(final TestTaskReporter reporter)
								throws Exception {
							Thread.sleep(!stack.empty() ? stack.pop() : 30);
						}

						@Override
						public TestTask nextTaskIfAny() {
							return stack.empty() ? null : newTask(stack);
						}

						@Override
						public TestTaskCategory category() {
							return Category.Sleep;
						}
					};
				}
			});
		}
		return list;
	}

	private void setupStack(final Stack<Integer> stack) {
		final Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 1000; i++) {
			stack.push(random(random, 60, 69));
		}
		for (int i = 0; i < 12000; i++) {
			stack.push(random(random, 30, 59));
		}
		for (int i = 0; i < 600; i++) {
			stack.push(random(random, 70, 79));
		}
		for (int i = 0; i < 12000; i++) {
			stack.push(random(random, 30, 59));
		}
		for (int i = 0; i < 300; i++) {
			stack.push(random(random, 80, 89));
		}
		for (int i = 0; i < 12000; i++) {
			stack.push(random(random, 30, 59));
		}
		for (int i = 0; i < 100; i++) {
			stack.push(random(random, 90, 100));
		}
		for (int i = 0; i < 12000; i++) {
			stack.push(random(random, 30, 59));
		}
	}

	private int random(final Random random, final int min, final int max) {
		final int i = random.nextInt(max);
		return i < min ? random(random, min, max) : i;
	}
}
