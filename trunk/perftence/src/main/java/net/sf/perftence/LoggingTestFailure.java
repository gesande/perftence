package net.sf.perftence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: move this to a separate module
public class LoggingTestFailure implements TestFailureNotifier {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoggingTestFailure.class);

	@Override
	public void testFailed(final Throwable t) {
		LOG.error("Test failed!", t);
	}

}
