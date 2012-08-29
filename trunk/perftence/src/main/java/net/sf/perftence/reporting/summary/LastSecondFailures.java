package net.sf.perftence.reporting.summary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.perftence.reporting.CustomFailureReporter;
import net.sf.perftence.reporting.FailedInvocations;
import net.sf.perftence.reporting.FailedInvocationsFactory;

public final class LastSecondFailures implements CustomFailureReporter,
        CustomIntermediateSummaryProvider {
    private final Map<Long, FailedInvocations> failures = Collections
            .synchronizedMap(new HashMap<Long, FailedInvocations>());
    private FailedInvocationsFactory failedInvocationsFactory;

    public LastSecondFailures(
            final FailedInvocationsFactory failedInvocationsFactory) {
        this.failedInvocationsFactory = failedInvocationsFactory;
    }

    @Override
    public synchronized void more(final Throwable t) {
        final long currentSecond = currentSecond();
        if (failures().containsKey(currentSecond)) {
            failures().get(currentSecond).more(t);
        } else {
            final FailedInvocations failed = failedInvocationsFactory()
                    .newInstance();
            failed.more(t);
            failures().put(currentSecond, failed);
        }
    }

    private FailedInvocationsFactory failedInvocationsFactory() {
        return this.failedInvocationsFactory;
    }

    private Map<Long, FailedInvocations> failures() {
        return this.failures;
    }

    private static long currentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public void provideIntermediateSummary(final IntermediateSummary summary) {
        final long lastSecond = currentSecond() - 1;
        if (failures().containsKey(lastSecond)) {
            failures().get(lastSecond).provideIntermediateSummary(summary);
        }
    }

}
