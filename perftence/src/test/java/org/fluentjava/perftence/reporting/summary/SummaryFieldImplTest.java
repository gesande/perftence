package org.fluentjava.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SummaryFieldImplTest {

    @Test
    public void nullValue() {
        final SummaryFieldImpl<String> field = newTestField("test-field");
        assertEquals("test-field", field.name());
        assertNull(field.value());
    }

    @Test
    public void withValue() {
        final SummaryFieldImpl<String> field = newTestFieldWithValue("test-field", "value");
        assertEquals("test-field", field.name());
        assertNotNull(field.value());
        assertEquals("value", field.value());
    }

    private static SummaryFieldImpl<String> newTestFieldWithValue(String fieldName, final String value) {
        return new SummaryFieldImpl<>(newFieldDefinition(fieldName), value);
    }

    private static SummaryFieldImpl<String> newTestField(String fieldName) {
        return new SummaryFieldImpl<>(newFieldDefinition(fieldName));
    }

    private static FieldDefinition newFieldDefinition(final String fullName) {
        return new FieldDefinition() {

            @Override
            public String fullName() {
                return fullName;
            }
        };
    }
}
