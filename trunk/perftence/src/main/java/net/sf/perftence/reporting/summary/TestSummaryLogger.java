package net.sf.perftence.reporting.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestSummaryLogger {

	private final static Logger LOG = LoggerFactory
			.getLogger(TestSummaryLogger.class);
	private final TestSummaryBuilder summary;

	TestSummaryLogger(final TestSummaryBuilder summary) {
		this.summary = summary;
	}

	private TestSummaryBuilder summaryBuilder() {
		return this.summary;
	}

	private String build() {
		return summaryBuilder().build();
	}

	public void printSummary(final String id) {
		printSummaryToLog(build(), id);
	}

	private void printSummaryToLog(final String summary, final String id) {
		if (hasSamples()) {
			logSummary(summary, id);
		} else {
			logNoSamplesReported();
		}
	}

	private boolean hasSamples() {
		return summaryBuilder().hasSamples();
	}

	private static void logSummary(final String summary, final String id) {
		log().info("{}{} statistics: {}{}",
				new Object[] { newLine(), id, newLine(), summary });
	}

	private static void logNoSamplesReported() {
		log().info("No samples, no need for the summary.");
	}

	private static Logger log() {
		return LOG;
	}

	private static String newLine() {
		return "\n";
	}

}
