package net.sf.perftence;

import java.util.Random;

import net.sf.perftence.api.PerftenceApi;
import net.sf.perftence.common.DefaultTestRuntimeReporterFactory;
import net.sf.perftence.fluent.MultithreadWorker;
import net.sf.perftence.fluent.TestBuilder;
import net.sf.perftence.graph.jfreechart.TestRuntimeReporterFactoryUsingJFreeChart;
import net.sf.perftence.reporting.Duration;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

public final class MainEntryPointWayOfDoingThings
		implements TestFailureNotifier {

	private final PerftenceApi api;
	private final static Random RANDOM = new Random(System.currentTimeMillis());

	public MainEntryPointWayOfDoingThings() {
		final TestRuntimeReporterFactoryUsingJFreeChart deps = new TestRuntimeReporterFactoryUsingJFreeChart();
		final DefaultTestRuntimeReporterFactory defaultTestRuntimeReporterFactory = new DefaultTestRuntimeReporterFactory(
				deps);
		this.api = new PerftenceApi(this, defaultTestRuntimeReporterFactory,
				deps.lineChartAdapterProvider(),
				deps.scatterPlotAdapterProvider());
	}

	public static void main(final String[] args) throws Exception {
		new MainEntryPointWayOfDoingThings().doThings();
	}

	private void doThings() throws Exception {
		final int duration = Duration.seconds(10);
		test1(duration).start();
		test2(duration).start();

		test("multiple-tests-concurrently").startable(test1(duration))
				.startable(test2(duration)).start();
	}

	private MultithreadWorker test2(final int duration) throws Exception {
		return test("test2")
				.setup(setup().duration(duration).threads(5).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						Thread.sleep(RANDOM.nextInt(50) + 1);
					}
				});
	}

	private MultithreadWorker test1(final int duration) throws Exception {
		return test("test1")
				.setup(setup().duration(duration).threads(10).build())
				.executable(new Executable() {
					@Override
					public void execute() throws Exception {
						Thread.sleep(RANDOM.nextInt(100) + 1);
					}
				});
	}

	private TestBuilder test(final String id) {
		return api().test(id);
	}

	private PerformanceTestSetupBuilder setup() {
		return api().setup();
	}

	private PerftenceApi api() {
		return this.api;
	}

	@Override
	public void testFailed(final Throwable t) {
		// no implementation
	}
}
