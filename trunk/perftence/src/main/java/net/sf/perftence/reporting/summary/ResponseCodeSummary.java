package net.sf.perftence.reporting.summary;

import java.text.DecimalFormat;
import java.util.Set;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.bag.TreeBag;

public class ResponseCodeSummary implements SummaryAppender {

    private SortedBag bag;
    private static final DecimalFormat DF = new DecimalFormat("###.###");

    public ResponseCodeSummary() {
        this.bag = SynchronizedSortedBag.decorate(new TreeBag());
    }

    public void report(final int responseCode) {
        bag().add(responseCode);
    }

    private SortedBag bag() {
        return this.bag;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void append(final Summary<?> summary) {
        summary.endOfLine().bold("Response code statistics:").endOfLine();
        final Set unique = bag().uniqueSet();
        for (Object value : unique) {
            summary.text("Response code : ").text(value.toString()).text(" ");
            summary.text("Frequency : ")
                    .text(Integer.toString(bag().getCount(value))).endOfLine();
        }
        int success_200 = bag().getCount(200);
        int success_204 = bag().getCount(204);
        int success_201 = bag().getCount(201);

        int successTotal = success_200 + success_204 + success_201;
        summary.text("Response success rate: ");
        if (bag().isEmpty()) {
            summary.text("[no responses]");
        } else {
            double successRate = (successTotal * 1.00 / bag().size()) * 100.00;
            summary.text(DF.format(successRate)).text(" %");
        }
        summary.endOfLine();
    }
}
