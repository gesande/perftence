package net.sf.perftence.reporting.summary;

import java.util.Set;

import net.sf.perftence.formatting.DefaultDoubleFormatter;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.bag.SynchronizedBag;

public final class FailedInvocations implements
        CustomSummaryProvider<HtmlSummary>, CustomIntermediateSummaryProvider {
    private final DefaultDoubleFormatter formatter;
    private final AdjustedFieldBuilder fieldBuilder;
    private final Bag exceptions;

    private double successRate;

    FailedInvocations(final AdjustedFieldBuilder fieldBuilder,
            final DefaultDoubleFormatter formatter) {
        this.fieldBuilder = fieldBuilder;
        this.formatter = formatter;
        this.exceptions = SynchronizedBag.decorate(new HashBag());
    }

    public void more(final Throwable t) {
        more(t.getClass().getName());
    }

    public void more(final String className) {
        exceptions().add(className);
    }

    public long failed() {
        return exceptions().size();
    }

    private Bag exceptions() {
        return this.exceptions;
    }

    @Override
    public String toString() {
        return "FailedInvocations [exceptions=" + exceptions() + "]";
    }

    @Override
    public void provideSummary(final Summary<HtmlSummary> summary) {
        summary.endOfLine();
        summary.text("Invocation success rate : ").text(format(successRate()))
                .text(" %").endOfLine();
        summary.endOfLine();
        summary.text("Error summary:").endOfLine();
        if (failed() == 0) {
            summary.text("No errors were reported").endOfLine();
            summary.endOfLine();
            return;
        }
        summary.note("Any allowed exception doesn't count for the measured latency statistics.");
        summary.text("Failed invocations : ").text(Long.toString(failed()))
                .endOfLine();
        summary.endOfLine();
        if (!uniqueExceptions().isEmpty()) {
            summary.text("Exception summary:").endOfLine();
            for (final String exceptionClass : uniqueExceptions()) {
                summary.text(exceptionClass).text(", count = ")
                        .text(exceptionCount(exceptionClass).toString())
                        .endOfLine();
            }
        }
    }

    @Override
    public void provideIntermediateSummary(final IntermediateSummary summary) {
        summary.field(failedInvocationsField());
        summary.endOfLine();
        if (!uniqueExceptions().isEmpty()) {
            summary.text("Exception summary:").endOfLine();
            for (final String exceptionClass : uniqueExceptions()) {
                summary.field(exceptionSummaryField(exceptionClass));
            }
            summary.endOfLine();
        }
    }

    private AdjustedField<Integer> exceptionSummaryField(
            final String exceptionClass) {
        return fieldBuilder().field(exceptionClass + ":",
                exceptionCount(exceptionClass));
    }

    private AdjustedField<Long> failedInvocationsField() {
        return fieldBuilder().field("failed invocations:", failed());
    }

    private AdjustedFieldBuilder fieldBuilder() {
        return this.fieldBuilder;
    }

    private String format(final double value) {
        return formatter().format(value);
    }

    private DefaultDoubleFormatter formatter() {
        return this.formatter;
    }

    private double successRate() {
        return this.successRate;
    }

    private Integer exceptionCount(final String clazz) {
        return exceptions().getCount(clazz);
    }

    @SuppressWarnings("unchecked")
    private Set<String> uniqueExceptions() {
        return exceptions().uniqueSet();
    }

    public CustomSummaryProvider<HtmlSummary> invocations(
            final long invocationCount) {
        this.successRate = calculateSuccessRate(invocationCount);
        return this;
    }

    private double calculateSuccessRate(final long invocationCount) {
        return ((double) invocationCount / (double) (invocationCount + failed())) * 100.00;
    }
}