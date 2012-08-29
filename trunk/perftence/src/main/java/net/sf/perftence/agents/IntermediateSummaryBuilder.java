package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.RuntimeStatisticsProvider;
import net.sf.perftence.reporting.summary.AbstractSummaryBuilder;
import net.sf.perftence.reporting.summary.AdjustedField;
import net.sf.perftence.reporting.summary.BuildableSummaryField;
import net.sf.perftence.reporting.summary.CustomIntermediateSummaryProvider;
import net.sf.perftence.reporting.summary.FieldDefinition;
import net.sf.perftence.reporting.summary.IntermediateSummary;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldBuilder;
import net.sf.perftence.reporting.summary.TestSummary;

final class IntermediateSummaryBuilder extends AbstractSummaryBuilder {

    private final ActiveThreads activeThreads;
    private final TaskSchedulingStatisticsProvider scheduledTasks;
    private final TestFailureNotifierDecorator failureNotifier;
    private final RuntimeStatisticsProvider statisticsProvider;
    private final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory;
    private List<CustomIntermediateSummaryProvider> customProviders;

    IntermediateSummaryBuilder(
            final RuntimeStatisticsProvider statisticsProvider,
            final ActiveThreads activeThreads,
            final TaskSchedulingStatisticsProvider scheduledTasks,
            final TestFailureNotifierDecorator failureNotifier,
            final SummaryFieldFactoryForAgentBasedTests summaryFieldFactory) {
        this.statisticsProvider = statisticsProvider;
        this.activeThreads = activeThreads;
        this.scheduledTasks = scheduledTasks;
        this.failureNotifier = failureNotifier;
        this.summaryFieldFactory = summaryFieldFactory;
        this.customProviders = new ArrayList<CustomIntermediateSummaryProvider>();
    }

    @Override
    public boolean hasSamples() {
        return statistics().hasSamples();
    }

    private RuntimeStatisticsProvider statistics() {
        return this.statisticsProvider;
    }

    private TestFailureNotifierDecorator failureNotifier() {
        return this.failureNotifier;
    }

    private ActiveThreads activeThreads() {
        return this.activeThreads;
    }

    private TaskSchedulingStatisticsProvider scheduledTasks() {
        return this.scheduledTasks;
    }

    @Override
    protected void fields(final TestSummary summary) {
        summary.field(finishedTasks(statistics().sampleCount()));
        summary.field(scheduledTasks(scheduledTasks().scheduledTasks()));
        summary.field(failedTasks(failureNotifier().failedTasks()));
        summary.field(threadsRunningCurrentTasks(activeThreads().active()));
        summary.field(max(statistics().maxLatency()));
        summary.field(average(statistics().averageLatency()).asFormatted());
        summary.field(median(statistics().median()));
        summary.field(percentile95(statistics().percentileLatency(95)));
        summary.field(throughput(statistics().currentThroughput())
                .asFormatted());
        summary.field(executionTime(statistics().currentDuration()));
        summary.field(lastTaskToBeRun(scheduledTasks()
                .lastTaskScheduledToBeRun()));
        customIntermediateSummary(summary);
    }

    private void customIntermediateSummary(final TestSummary summary) {
        for (final CustomIntermediateSummaryProvider provider : customProviders()) {
            provider.provideIntermediateSummary(new IntermediateSummary() {

                @Override
                public IntermediateSummary endOfLine() {
                    summary.endOfLine();
                    return this;
                }

                @Override
                public IntermediateSummary text(final String text) {
                    summary.text(text);
                    return this;
                }

                @Override
                public IntermediateSummary field(final AdjustedField<?> field) {
                    summary.field(summaryFieldFactory()
                            .custom(toFieldDefinition(field.name()),
                                    Object.class).value(field.value()).build());
                    return this;
                }

                private FieldDefinition toFieldDefinition(final String name) {
                    return new FieldDefinition() {

                        @Override
                        public String fullName() {
                            return name;
                        }
                    };
                }
            });
        }
    }

    public IntermediateSummaryBuilder customSummaryProviders(
            final CustomIntermediateSummaryProvider... providers) {
        for (final CustomIntermediateSummaryProvider provider : providers) {
            customProviders().add(provider);
        }
        return this;
    }

    private List<CustomIntermediateSummaryProvider> customProviders() {
        return this.customProviders;
    }

    private SummaryField<?> executionTime(final long currentDuration) {
        return summaryFieldFactory().executionTime(currentDuration);
    }

    private BuildableSummaryField<Double> throughput(
            final double currentThroughput) {
        return summaryFieldFactory().throughput(currentThroughput);
    }

    private SummaryField<Long> percentile95(long percentileLatency) {
        return summaryFieldFactory().percentile95(percentileLatency);
    }

    private SummaryField<Long> median(final long median) {
        return summaryFieldFactory().median(median);
    }

    private BuildableSummaryField<Double> average(final double averageLatency) {
        return summaryFieldFactory().average(averageLatency);
    }

    private SummaryField<Long> max(final long maxLatency) {
        return summaryFieldFactory().max(maxLatency);
    }

    private SummaryField<String> lastTaskToBeRun(final Time time) {
        return summaryFieldFactory().lastTaskToBeRun(time);
    }

    private SummaryField<Long> failedTasks(final long value) {
        return summaryFieldFactory().failedTasks(value);
    }

    private SummaryField<Integer> scheduledTasks(final int value) {
        return summaryFieldFactory().scheduleTasks(value);
    }

    private SummaryField<Long> finishedTasks(final long value) {
        return summaryFieldFactory().finishedTasks(value);
    }

    private SummaryField<Integer> threadsRunningCurrentTasks(final int value) {
        return summaryFieldFactory().threadsRunningCurrentTasks(value);
    }

    private SummaryFieldFactoryForAgentBasedTests summaryFieldFactory() {
        return this.summaryFieldFactory;
    }

    public <VALUE> SummaryFieldBuilder<VALUE> custom(
            final FieldDefinition field, final Class<VALUE> valueType) {
        return summaryFieldFactory().custom(field, valueType);
    }

}