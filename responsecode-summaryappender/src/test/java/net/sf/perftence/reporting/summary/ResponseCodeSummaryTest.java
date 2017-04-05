package net.sf.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;

import org.junit.Test;

public class ResponseCodeSummaryTest {
	private final static DecimalFormat DF = new DecimalFormat("###.###");

	@Test
	public void summary() {
		final ResponseCodeSummary summary = new ResponseCodeSummary();
		for (int i = 0; i < 7122; i++) {
			summary.report(200);
		}
		summary.report(404);
		final StringBuilder sb = new StringBuilder();
		summary.append(logSummary(sb));
		System.out.print("Result should be like this: \n" + sb.toString());
		final String result = "ENDOFLINE\n"
				+ "BOLDResponse code statistics:ENDOFLINE\n"
				+ "TEXTResponse code : TEXT200TEXT TEXTFrequency : TEXT7122ENDOFLINE\n"
				+ "TEXTResponse code : TEXT404TEXT TEXTFrequency : TEXT1ENDOFLINE\n"
				+ "TEXTResponse success rate: TEXT" + DF.format(99.986)
				+ "TEXT %ENDOFLINE\n";
		System.out.print("Result was: \n" + result);
		assertEquals(result, sb.toString());
		// TODO: asserts
	}

	@Test
	public void empty() {
		final ResponseCodeSummary summary = new ResponseCodeSummary();
		final StringBuilder sb = new StringBuilder();
		summary.append(logSummary(sb));
		System.out.print("Result should be like this: \n" + sb.toString());
		final String result = "ENDOFLINE\n"
				+ "BOLDResponse code statistics:ENDOFLINE\n"
				+ "TEXTResponse success rate: TEXT[no responses]"
				+ "ENDOFLINE\n";
		System.out.print("Result was: \n" + result);
		assertEquals(result, sb.toString());

	}

	private static Summary<ResponseCodeSummaryTest> logSummary(
			final StringBuilder sb) {
		return new Summary<ResponseCodeSummaryTest>() {

			@Override
			public Summary<ResponseCodeSummaryTest> text(final String text) {
				sb.append("TEXT").append(text);
				return this;
			}

			@Override
			public Summary<ResponseCodeSummaryTest> endOfLine() {
				sb.append("ENDOFLINE").append("\n");
				return this;
			}

			@Override
			public Summary<ResponseCodeSummaryTest> bold(final String text) {
				sb.append("BOLD").append(text);
				return this;
			}

			@Override
			public Summary<ResponseCodeSummaryTest> note(String text) {
				sb.append("NOTE").append(text);
				return this;
			}
		};
	}
}
