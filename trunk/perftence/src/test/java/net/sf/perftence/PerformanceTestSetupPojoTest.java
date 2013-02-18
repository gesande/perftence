package net.sf.perftence;

import org.junit.Test;

public class PerformanceTestSetupPojoTest {

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupDuration() {
        PerformanceTestSetupPojo.builder().noSetup().duration();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupGraphWriters() {
        PerformanceTestSetupPojo.builder().noSetup().graphWriters();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupInvocationRange() {
        PerformanceTestSetupPojo.builder().noSetup().invocationRange();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupInvocations() {
        PerformanceTestSetupPojo.builder().noSetup().invocations();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupSummaryAppenders() {
        PerformanceTestSetupPojo.builder().noSetup().summaryAppenders();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupThreads() {
        PerformanceTestSetupPojo.builder().noSetup().threads();
    }

    @SuppressWarnings("static-method")
    @Test(expected = NoTestSetupDefined.class)
    public void noSetupThroughputRange() {
        PerformanceTestSetupPojo.builder().noSetup().throughputRange();
    }

}
