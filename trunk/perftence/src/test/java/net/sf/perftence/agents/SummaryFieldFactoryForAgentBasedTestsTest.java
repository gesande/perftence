package net.sf.perftence.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.sf.perftence.formatting.FieldFormatter;
import net.sf.perftence.reporting.summary.FieldAdjuster;
import net.sf.perftence.reporting.summary.SummaryField;
import net.sf.perftence.reporting.summary.SummaryFieldFactory;

import org.junit.Test;

public class SummaryFieldFactoryForAgentBasedTestsTest {

    @Test
    public void lastTaskToBeRun() {
        final SummaryFieldFactoryForAgentBasedTests factory = newFactory();
        final SummaryField<String> lastTaskToBeRun = factory
                .lastTaskToBeRun(TimeSpecificationFactory.inMillis(1000));
        assertNotNull(lastTaskToBeRun.value());
        assertEquals("in 1000 (ms)", lastTaskToBeRun.value());
        assertEquals("last task to be run:     ", lastTaskToBeRun.name());

    }

    @Test
    public void lastTaskToBeRunTimeNotAvailable() {
        final SummaryFieldFactoryForAgentBasedTests factory = newFactory();
        final SummaryField<String> lastTaskToBeRun = factory
                .lastTaskToBeRun(null);
        assertNotNull(lastTaskToBeRun.value());
        assertEquals("<not available>", lastTaskToBeRun.value());
        assertEquals("last task to be run:     ", lastTaskToBeRun.name());
    }

    @SuppressWarnings("static-method")
    private SummaryFieldFactoryForAgentBasedTests newFactory() {
        return new SummaryFieldFactoryForAgentBasedTests(
                SummaryFieldFactory.create(new FieldFormatter(),
                        new FieldAdjuster()));
    }
}
