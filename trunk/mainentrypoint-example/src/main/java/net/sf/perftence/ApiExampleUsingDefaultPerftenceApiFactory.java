package net.sf.perftence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.api.DefaultPerftenceApiFactory;
import net.sf.perftence.api.PerftenceApi;
import net.sf.perftence.reporting.Duration;

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
        api.test("api-example").setup(api.setup().threads(2).duration(Duration.seconds(10)).build())
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
    }

    private static PerftenceApi perftenceApi(TestFailureNotifier notifier) {
        return new DefaultPerftenceApiFactory().newPerftenceApi(notifier);
    }

    private static Logger log() {
        return LOG;
    }
}
