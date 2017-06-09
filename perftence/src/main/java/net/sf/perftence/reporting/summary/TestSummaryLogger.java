package net.sf.perftence.reporting.summary;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestSummaryLogger {
    private final static Logger LOG = LoggerFactory.getLogger(TestSummaryLogger.class);
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

    public void printSummary(final String id, Consumer<String> summaryConsumer) {
        String summary = build();
        printSummary(id, summary);
        if (summaryConsumer != null) {
            summaryConsumer.accept(summary);
        }
    }

    private void printSummary(final String id, final String summary) {
        if (hasSamples())
            logSummary(id, summary);
        else
            logNoSamples(id);
    }

    private boolean hasSamples() {
        return summaryBuilder().hasSamples();
    }

    private static void logSummary(final String id, final String summary) {
        log().info("{}{} statistics: {}{}", new Object[] { newLine(), id, newLine(), summary });
    }

    private static void logNoSamples(String id) {
        log().info("No samples for {}, no need for the summary.", id);
    }

    private static String newLine() {
        return "\n";
    }

    private static Logger log() {
        return LOG;
    }

}
