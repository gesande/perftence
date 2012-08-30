package net.sf.perftence.reporting.summary;

import org.junit.Test;

public class ResponseCodeSummaryTest {

    /*
     * Response code statistics:
     * 
     * Response code : 200 Frequency : 7122
     * 
     * Response code : 404 Frequency : 1
     * 
     * Response success rate: 99.986 %
     */
    @Test
    public void test() {
        final ResponseCodeSummary summary = new ResponseCodeSummary();
        for (int i = 0; i < 7122; i++) {
            summary.report(200);
        }
        summary.report(404);
        summary.append(logSummary());
        // TODO: asserts
    }

    private Summary<ResponseCodeSummaryTest> logSummary() {
        return new Summary<ResponseCodeSummaryTest>() {

            @Override
            public Summary<ResponseCodeSummaryTest> text(final String text) {
                System.out.print(text);
                return this;
            }

            @Override
            public Summary<ResponseCodeSummaryTest> endOfLine() {
                System.out.println();
                return this;
            }

            @Override
            public Summary<ResponseCodeSummaryTest> bold(final String text) {
                System.out.println(text);
                return this;
            }

            @Override
            public Summary<ResponseCodeSummaryTest> note(String text) {
                return this;
            }

        };
    }
}
