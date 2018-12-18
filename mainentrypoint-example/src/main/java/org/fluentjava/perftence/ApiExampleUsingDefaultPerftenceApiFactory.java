package org.fluentjava.perftence;

import org.fluentjava.perftence.api.DefaultPerftenceApiFactory;
import org.fluentjava.perftence.api.PerftenceApi;
import org.fluentjava.perftence.reporting.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiExampleUsingDefaultPerftenceApiFactory {

    private final static Logger LOG = LoggerFactory.getLogger(ApiExampleUsingDefaultPerftenceApiFactory.class);

    public static void main(final String[] args) throws Exception {
        new ApiExampleUsingDefaultPerftenceApiFactory().run();
    }

    public void run() {
        final TestFailureNotifier notifier = new TestFailureNotifier() {
            @Override
            public void testFailed(Throwable t) {
                log().error("Test failed!", t);
            }
        };
        final PerftenceApi api = perftenceApi(notifier);
        Executable myMeasurableItem = () -> Thread.sleep(100);
        api.test("api-example").setup(api.setup().threads(2).duration(Duration.seconds(10)).build())
                .executable(myMeasurableItem).start();
    }

    private static PerftenceApi perftenceApi(TestFailureNotifier notifier) {
        return new DefaultPerftenceApiFactory().newPerftenceApi(notifier);
    }

    private static Logger log() {
        return LOG;
    }
}
