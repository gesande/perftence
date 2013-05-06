package net.sf.perftence;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTestFailure implements TestFailureNotifier {

    private static final Logger LOG = LoggerFactory
            .getLogger(LoggingTestFailure.class);

    @Override
    public void testFailed(final Throwable t) {
        LOG.error("Test failed!", t);
    }

}
