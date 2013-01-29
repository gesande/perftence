package net.sf.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SummaryFieldBuilderForSingleValueTest {

    @SuppressWarnings("static-method")
    @Test
    public void noFieldDefinitionNoValue() {
        final SummaryFieldBuilderForSingleValue<Double> builder = new SummaryFieldBuilderForSingleValue<Double>(
                new FieldFormatter(), new FieldAdjuster());
        final SummaryField<Double> field = builder.build();
        assertNull(field.value());
        assertEquals("<no name>                ", field.name());
        SummaryField<String> formatted = builder.asFormatted();
        assertEquals("<value was null>", formatted.value());
        assertEquals("<no name>                ", formatted.name());
    }

    @SuppressWarnings("static-method")
    @Test
    public void noFieldDefinitionButHasAValue() {
        final BuildableSummaryField<Double> buildable = new SummaryFieldBuilderForSingleValue<Double>(
                new FieldFormatter(), new FieldAdjuster()).value(10.01);
        final SummaryField<Double> field = buildable.build();
        assertNotNull(field.value());
        assertEquals("<no name>                ", field.name());
        SummaryField<String> formatted = buildable.asFormatted();
        assertEquals("10.01", formatted.value());
        assertEquals("<no name>                ", formatted.name());
    }

    @SuppressWarnings("static-method")
    @Test
    public void hasFieldDefinitionAndAValue() {
        final BuildableSummaryField<Double> buildable = new SummaryFieldBuilderForSingleValue<Double>(
                new FieldFormatter(), new FieldAdjuster()).field(
                new FieldDefinition() {
                    @Override
                    public String fullName() {
                        return "double field";
                    }
                }).value(10.01);
        final SummaryField<Double> field = buildable.build();
        assertNotNull(field.value());
        assertEquals("double field             ", field.name());
        SummaryField<String> formatted = buildable.asFormatted();
        assertEquals("10.01", formatted.value());
        assertEquals("double field             ", formatted.name());

    }
}
