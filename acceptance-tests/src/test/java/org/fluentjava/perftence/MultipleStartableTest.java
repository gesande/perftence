package org.fluentjava.perftence;

import java.util.Random;

import org.fluentjava.perftence.fluent.MultithreadWorker;
import org.fluentjava.perftence.fluent.TestBuilder;
import org.fluentjava.perftence.reporting.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class MultipleStartableTest extends AbstractMultiThreadedTest {
    private static final Random RANDOM = new Random();

    @Test
    public void execute() throws Exception {
        test().startable(threadWorker(idOne(), 10, 100)).startable(threadWorker(idTwo(), 15, 100))
                .startable(threadWorker(idThree(), 20, 100)).start();
    }

    @Test
    public void duration() throws Exception {
        test().startable(durationWorker(idOne(), 10)).startable(durationWorker(idTwo(), 10))
                .startable(durationWorker(idThree(), 10)).start();
    }

    @Test
    public void mixed() throws Exception {
        test().startable(durationWorker(idOne(), 10)).startable(threadWorker(idTwo(), 10, 100)).start();
    }

    private String idThree() {
        return id() + ".3";
    }

    private String idTwo() {
        return id() + ".2";
    }

    private String idOne() {
        return id() + ".1";
    }

    private MultithreadWorker threadWorker(final String id1, final int threads, final int invocations)
            throws Exception {
        return test(id1).setup(setup().threads(threads).invocations(invocations).build())
                .executable(sleepingExecutable());
    }

    private MultithreadWorker durationWorker(final String id, final int threads) throws Exception {
        return durationWorkerBuilder(id, threads).executable(sleepingExecutable());
    }

    private TestBuilder durationWorkerBuilder(final String id, final int threads) {
        return test(id).setup(setup().threads(threads).duration(Duration.seconds(10)).build());
    }

    private static Executable sleepingExecutable() {
        return new Executable() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(RANDOM.nextInt(10) + 1);
            }
        };
    }

}
