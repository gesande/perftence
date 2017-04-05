package net.sf.perftence.reporting.summary;

public final class TestSummaryLoggerFactory {

	@SuppressWarnings("static-method")
	public TestSummaryLogger newSummaryLogger(
			final TestSummaryBuilder builder) {
		return new TestSummaryLogger(builder);
	}
}
