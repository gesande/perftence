package net.sf.perftence.agents;

import junit.framework.Assert;
import net.sf.perftence.TestFailureNotifier;

import org.junit.Test;

public class SchedulingServiceBasedOnTaskProviderTest {

    @SuppressWarnings("static-method")
    @Test
    public void schedule() {
        SchedulingServiceFactory.newBasedOnTaskProvider(inMillis(100),
                new RunnableAdapter() {

                    @Override
                    public Runnable adapt(final TestTask task,
                            long timeItWasScheduled) {
                        return new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    task.run(null);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                    }
                }, 10, new TestFailureNotifier() {
                    @Override
                    public void testFailed(Throwable t) {
                        Assert.fail("Test failed : " + t.getMessage());
                    }
                });
    }

    private static Time inMillis(long time) {
        return TimeSpecificationFactory.inMillis(time);
    }
}
