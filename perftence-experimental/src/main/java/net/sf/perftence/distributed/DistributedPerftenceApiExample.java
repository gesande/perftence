package net.sf.perftence.distributed;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.perftence.Executable;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.fluent.PerformanceRequirementsPojo;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;
import net.sf.perftence.reporting.LatencyReporter;
import net.sf.perftence.setup.PerformanceTestSetupPojo;
import net.sf.perftence.setup.PerformanceTestSetupPojo.PerformanceTestSetupBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedPerftenceApiExample {
    private final static Logger LOG = LoggerFactory
            .getLogger(DistributedPerftenceApiExample.class);

    public static void main(String[] args) throws MalformedURLException {
        new DistributedPerftenceApiExample().exampleOfDistributedTest();
    }

    public void exampleOfDistributedTest() throws MalformedURLException {
        final DistributedLatencyReporterFactory reporterFactory = new DistributedLatencyReporterFactory() {

            @Override
            public LatencyReporter forRemoteReporting(final URL reportsTo) {
                return new LatencyReporter() {

                    @Override
                    public void latency(final int latency) {
                        LOG.info("reporting latency " + latency + " remotely "
                                + reportsTo.toExternalForm());
                    }
                };
            }

            @Override
            public LatencyReporter forLocalReporting() {
                return new LatencyReporter() {

                    @Override
                    public void latency(final int latency) {
                        LOG.info("reporting latency " + latency + " locally");
                    }

                };
            }
        };
        final DistributedPerftenceApi api = new DistributedPerftenceApi(
                new TestFailureNotifier() {

                    @Override
                    public void testFailed(Throwable t) {
                        LOG.error("Test failed!", t);
                    }
                }, reporterFactory).reportingLocally().reportsLatenciesTo(
                new URL("http://localhost:9001/report/latency"));
        api.test(id()).setup(setup().threads(10).invocations(100).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);

                    }
                }).start();
    }

    @SuppressWarnings("static-method")
    private String id() {
        return "distributed-perftence-test";
    }

    @SuppressWarnings({ "unused", "static-method" })
    private PerformanceRequirementsBuilder requirements() {
        return PerformanceRequirementsPojo.builder();
    }

    @SuppressWarnings("static-method")
    private PerformanceTestSetupBuilder setup() {
        return PerformanceTestSetupPojo.builder();
    }
}
