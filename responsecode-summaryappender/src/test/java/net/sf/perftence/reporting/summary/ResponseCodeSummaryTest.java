package net.sf.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResponseCodeSummaryTest {

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
                + "TEXTResponse success rate: TEXT99,986TEXT %ENDOFLINE\n";
        assertEquals(result, sb.toString());
        // TODO: asserts
    }

    private Summary<ResponseCodeSummaryTest> logSummary(final StringBuilder sb) {
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
