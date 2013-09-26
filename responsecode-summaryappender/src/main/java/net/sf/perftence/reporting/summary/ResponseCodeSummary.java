package net.sf.perftence.reporting.summary;

import java.text.DecimalFormat;
import java.util.Collection;

import net.sf.völundr.bag.StronglyTypedSortedBag;

public class ResponseCodeSummary implements SummaryAppender {

    private StronglyTypedSortedBag<Integer> bag;
    private static final DecimalFormat DF = new DecimalFormat("###.###");

    public ResponseCodeSummary() {
        this.bag = StronglyTypedSortedBag.synchronizedTreeBag();
    }

    public void report(final int responseCode) {
        bag().add(responseCode);
    }

    private StronglyTypedSortedBag<Integer> bag() {
        return this.bag;
    }

    @Override
    public void append(final Summary<?> summary) {
        summary.endOfLine().bold("Response code statistics:").endOfLine();
        final Collection<Integer> unique = bag().uniqueSamples();
        for (final Integer value : unique) {
            summary.text("Response code : ").text(value.toString()).text(" ");
            summary.text("Frequency : ")
                    .text(Integer.toString(bag().count(value))).endOfLine();
        }
        int success_200 = bag().count(200);
        int success_204 = bag().count(204);
        int success_201 = bag().count(201);

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
