package net.sf.perftence.common;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import net.sf.perftence.graph.GraphWriter;
import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.reporting.TestReport;
import net.sf.perftence.reporting.TestRuntimeReporter;
import net.sf.perftence.reporting.summary.FailedInvocations;
import net.sf.perftence.reporting.summary.HtmlSummary;
import net.sf.perftence.reporting.summary.StatisticsSummaryProvider;
import net.sf.perftence.reporting.summary.SummaryAppender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DefaultTestRuntimeReporter implements TestRuntimeReporter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultTestRuntimeReporter.class);

	private final int threadCount;
	private final int duration;
	private final InvocationStorage invocationStorage;
	private final ImageFactory imageFactory;
	private final ThroughputStorage throughputStorage;
	private final FrequencyStorage frequencyStorage;
	private final boolean includeInvocationGraph;
	private final StatisticsSummaryProvider<HtmlSummary> statisticsProvider;
	private final FailedInvocations failedInvocations;
	private final Collection<GraphWriter> graphWriters;
	private final Collection<SummaryAppender> customSummaryAppenders;
	private final TestReport testReport;

	public DefaultTestRuntimeReporter(
			final InvocationStorage invocationStorage,
			final ThroughputStorage throughputStorage,
			final ImageFactory imageFactory, final int threadCount,
			final int duration, final FrequencyStorage frequencyStorage,
			final Collection<SummaryAppender> summaryAppenders,
			final boolean includeInvocationGraph,
			final Collection<GraphWriter> graphWriters,
			final StatisticsSummaryProvider<HtmlSummary> statisticsProvider,
			final FailedInvocations failedInvocations,
			final TestReport testReport) {
		this.invocationStorage = invocationStorage;
		this.throughputStorage = throughputStorage;
		this.imageFactory = imageFactory;
		this.threadCount = threadCount;
		this.duration = duration;
		this.frequencyStorage = frequencyStorage;
		this.customSummaryAppenders = summaryAppenders;
		this.includeInvocationGraph = includeInvocationGraph;
		this.graphWriters = graphWriters;
		this.statisticsProvider = statisticsProvider;
		this.failedInvocations = failedInvocations;
		this.testReport = testReport;
	}

	@Override
	public void throughput(final long currentDuration, final double throughput) {
		throughputStorage().store(currentDuration, throughput);
	}

	private StringBuilder createSummary(final String id,
			final long elapsedTime, final long invocationCount) {
		final HtmlSummary summary = startSummary();
		buildSummary(id, elapsedTime, invocationCount, summary);
		appendInvocationChart(id, summary);
		appendThroughputChart(id + "-throughput", summary);
		appendFrequencyChart(id + "-frequency", summary);
		appendCustomGraphs(summary);
		return summary.end();
	}

	private void appendCustomGraphs(final HtmlSummary summary) {
		for (final GraphWriter graphWriter : graphWriters()) {
			if (graphWriter.hasSomethingToWrite()) {
				final String id = graphWriter.id();
				graphWriter.writeImage(imageFactory());
				appendImage(id, summary);
			}
		}
	}

	private Collection<GraphWriter> graphWriters() {
		return this.graphWriters;
	}

	private static HtmlSummary startSummary() {
		return new HtmlSummary().start();
	}

	private void appendFrequencyChart(final String id, final HtmlSummary summary) {
		if (writeFrequencyChart(id)) {
			appendImage(id, summary);
		}
	}

	private boolean writeFrequencyChart(final String id) {
		if (frequencyStorage().hasSamples()) {
			imageFactory()
					.createXYLineChart(id, frequencyStorage().imageData());
			return true;
		}
		return false;
	}

	private FrequencyStorage frequencyStorage() {
		return this.frequencyStorage;
	}

	private void appendThroughputChart(final String id,
			final HtmlSummary summary) {
		if (writeThroughputChart(id)) {
			appendImage(id, summary);
		}
	}

	private static void appendImage(final String id, final HtmlSummary summary) {
		summary.image(id);
	}

	private boolean writeThroughputChart(final String id) {
		if (throughputStorage().isEmpty()) {
			log().info("No data to print to the throughput chart");
			return false;
		}
		imageFactory().createXYLineChart(id, throughputStorage().imageData());
		return true;
	}

	private ThroughputStorage throughputStorage() {
		return this.throughputStorage;
	}

	private void appendInvocationChart(final String id,
			final HtmlSummary summary) {
		if (includeInvocationGraph() && writeInvocationChart(id)) {
			appendImage(id, summary);
		}
	}

	@Override
	public boolean includeInvocationGraph() {
		return this.includeInvocationGraph;
	}

	private boolean writeInvocationChart(final String id) {
		if (invocationStorage().isEmpty()) {
			log().info("No data to print to the invocation chart for {}", id);
			return false;
		}
		imageFactory().createXYLineChart(id, invocationStorage().imageData());
		return true;
	}

	private static Logger log() {
		return LOGGER;
	}

	private ImageFactory imageFactory() {
		return this.imageFactory;
	}

	private InvocationStorage invocationStorage() {
		return this.invocationStorage;
	}

	private void writeSummary(final String id, final StringBuilder sb) {
		testReport().writeSummary(id, sb.toString());
	}

	private void buildSummary(final String name, final long elapsedTime,
			final long invocationCount, final HtmlSummary summary) {
		summary.header(name).endOfLine();
		summary.testReportCreated(now()).br();
		summary.numberOfInvocations(invocationCount).endOfLine();
		summary.totalTime(elapsedTime, " ms").endOfLine();
		if (threadCount() > 1) {
			summary.threadCount(threadCount()).endOfLine();
		}
		if (duration() > 1) {
			summary.duration(duration()).endOfLine();
		}
		statisticsSummaryProvider().elapsedTime(elapsedTime)
				.invocations(invocationCount).provideSummary(summary);
		failedInvocations().invocations(invocationCount)
				.provideSummary(summary);
		if (reportedLatencyBeingBelowOne()) {
			summary.note()
					.text("Test reported about some invocations being less than one millisecond.")
					.endOfLine()
					.text("That is always calculated as one millisecond instead of zero.")
					.endOfLine();
		}
		summary.endOfLine();
		appendCustomSummaries(summary);
	}

	private void appendCustomSummaries(final HtmlSummary summary) {
		for (SummaryAppender custom : customSummaryAppenders()) {
			custom.append(summary);
		}
	}

	private Collection<SummaryAppender> customSummaryAppenders() {
		return this.customSummaryAppenders;
	}

	private StatisticsSummaryProvider<HtmlSummary> statisticsSummaryProvider() {
		return this.statisticsProvider;
	}

	private int duration() {
		return this.duration;
	}

	private int threadCount() {
		return this.threadCount;
	}

	private static Date now() {
		return Calendar.getInstance().getTime();
	}

	private boolean reportedLatencyBeingBelowOne() {
		return invocationStorage().reportedLatencyBeingBelowOne();
	}

	@Override
	public void summary(final String id, final long elapsedTime,
			final long sampleCount, final long startTime) {
		if (invocationStorage().reportedLatencyBeingBelowOne()) {
			log().info("Reported some of the latencies being less than 1 ms.");
		}
		if (sampleCount == 0) {
			log().info(
					"No need for summary because no invocations were reported for {}.",
					id);
			return;
		}
		writeSummary(id, createSummary(id, elapsedTime, sampleCount));
		updateIndexFile(id);
	}

	private void updateIndexFile(final String id) {
		testReport().updateIndexFile(id);
	}

	@Override
	public synchronized void latency(final int latency) {
		invocationStorage().store(latency);
	}

	@Override
	public void invocationFailed(final Throwable t) {
		failedInvocations().more(t);
	}

	private FailedInvocations failedInvocations() {
		return this.failedInvocations;
	}

	private TestReport testReport() {
		return this.testReport;
	}

}
