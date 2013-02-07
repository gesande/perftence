package net.sf.perftence.experimental;

import net.sf.perftence.Executable;
import net.sf.perftence.PerftenceApi;
import net.sf.perftence.TestFailureNotifier;
import net.sf.perftence.reporting.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiExample {

    private final static Logger LOG = LoggerFactory.getLogger(ApiExample.class);

    public static void main(final String[] args) throws Exception {
        new ApiExample().run();
    }

    @SuppressWarnings("static-method")
    public void run() {
        final PerftenceApi api = new PerftenceApi(new TestFailureNotifier() {
            @Override
            public void testFailed(Throwable t) {
                log().error("Test failed!", t);
            }
        });
        api.test("api-example")
                .setup(api.setup().threads(2).duration(Duration.seconds(10))
                        .build()).executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(100);
                    }
                }).start();
    }

    private static Logger log() {
        return LOG;
    }
}
