package net.sf.perftence.agents;

import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.CustomSummaryFieldProvider;
import net.sf.perftence.reporting.summary.FieldDefinition;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldBuilder;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;

public final class SummaryFieldFactoryForAgentBasedTests implements CustomSummaryFieldProvider {

    private final SummaryFieldFactory summaryFieldFactory;

    public SummaryFieldFactoryForAgentBasedTests(final SummaryFieldFactory summaryFieldFactory) {
        this.summaryFieldFactory = summaryFieldFactory;
    }

    private SummaryFieldFactory summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    private enum Fields implements FieldDefinition {
        FailedTasks {

            @Override
            public String fullName() {
                return "failed tasks:";
            }
        },
        ScheduledTasks {

            @Override
            public String fullName() {
                return "scheduled tasks:";
            }
        },
        FinishedTasks {

            @Override
            public String fullName() {
                return "finished tasks:";
            }
        },
        ThreadsRunningTasks {

            @Override
            public String fullName() {
                return "threads running tasks:";
            }
        },
        LastTaskToBeRun {

            @Override
            public String fullName() {
                return "last task to be run: ";
            }
        }
    }

    public SummaryField<String> lastTaskToBeRun(final Time time) {
        return summaryFieldFactory().custom(Fields.LastTaskToBeRun, String.class).value(time == null ? "<not available>"
                : "in " + Long.toString(TimeSpecificationFactory.toMillis(time)) + " (ms)").build();
    }

    public SummaryField<Long> failedTasks(final long value) {
        return summaryFieldFactory().custom(Fields.FailedTasks, Long.class).value(value).build();
    }

    public SummaryField<Integer> scheduleTasks(final int value) {
        return summaryFieldFactory().custom(Fields.ScheduledTasks, Integer.class).value(value).build();
    }

    public SummaryField<Long> finishedTasks(final long value) {
        return summaryFieldFactory().custom(Fields.FinishedTasks, Long.class).value(value).build();
    }

    public SummaryField<Integer> threadsRunningCurrentTasks(final int value) {
        return summaryFieldFactory().custom(Fields.ThreadsRunningTasks, Integer.class).value(value).build();
    }

    public SummaryField<Long> executionTime(long duration) {
        return summaryFieldFactory().executionTime().value(duration).build();
    }

    public BuildableSummaryField<Double> throughput(double value) {
        return summaryFieldFactory().throughput().value(value);
    }

    public SummaryField<Long> percentile95(final long value) {
        return summaryFieldFactory().percentile95().value(value).build();
    }

    public SummaryField<Long> median(final long value) {
        return summaryFieldFactory().median().value(value).build();
    }

    public BuildableSummaryField<Double> average(final double averageLatency) {
        return summaryFieldFactory().average().value(averageLatency);
    }

    public SummaryField<Long> max(final long maxLatency) {
        return summaryFieldFactory().max().value(maxLatency).build();
    }

    @Override
    public <VALUE> SummaryFieldBuilder<VALUE> custom(final FieldDefinition field, final Class<VALUE> valueType) {
        return summaryFieldFactory().custom(field, valueType);
    }

}
