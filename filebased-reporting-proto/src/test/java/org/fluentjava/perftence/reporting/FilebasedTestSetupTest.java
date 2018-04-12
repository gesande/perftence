package org.fluentjava.perftence.reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.fluentjava.perftence.setup.PerformanceTestSetup;
import org.fluentjava.perftence.setup.PerformanceTestSetupPojo;
import org.junit.Test;

public class FilebasedTestSetupTest {

    @Test
    public void serialize() throws IOException {
        PerformanceTestSetup setup = PerformanceTestSetupPojo.builder().threads(100).invocations(1000)
                .invocationRange(1000).throughputRange(200).build();
        final FilebasedTestSetup myObject = new FilebasedTestSetup(setup, true);
        // Write to disk with FileOutputStream
        final FileOutputStream f_out = new FileOutputStream(new File("target", "filebasedTestSetup.data"));
        final ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
        try {
            // Write object with ObjectOutputStream
            // Write object out to disk
            obj_out.writeObject(myObject);
        } finally {
            obj_out.close();
            f_out.close();
        }
    }

    @Test
    public void deserialize() throws IOException, ClassNotFoundException {
        // Read from disk using FileInputStream
        final InputStream f_in = FilebasedTestSetupTest.class.getResourceAsStream("/" + "filebasedTestSetup.data");
        // Read object using ObjectInputStream
        final ObjectInputStream obj_in = new ObjectInputStream(f_in);
        try {
            // Read an object
            final FilebasedTestSetup obj = (FilebasedTestSetup) obj_in.readObject();
            assertTrue(obj.includeInvocationGraph());
            assertNotNull(obj.testSetup());
            assertEquals(100, obj.testSetup().threads());
            assertEquals(1000, obj.testSetup().invocations());
            assertEquals(1000, obj.testSetup().invocationRange());
            assertEquals(200, obj.testSetup().throughputRange());
        } finally {
            obj_in.close();
            f_in.close();
        }
    }
}
