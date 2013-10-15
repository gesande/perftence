package net.sf.perftence.reporting;

public interface SummaryReporter {
	void summary(final String id, final long elapsedTime,
			final long sampleCount, final long startTime);
}
