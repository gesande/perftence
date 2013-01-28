package net.sf.perftence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.perftence.fluent.MultithreadWorker;
import net.sf.perftence.fluent.TestBuilder;
import net.sf.perftence.reporting.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class MultipleStartableTest extends AbstractMultiThreadedTest {
    private static final Random RANDOM = new Random();

    @Test
    public void noInvocationGraphConstruction() throws Exception {
        final AtomicBoolean executed = new AtomicBoolean(false);
        final MultithreadWorker durationWorker = test(id() + ".1")
                .setup(setup().threads(10).duration(Duration.seconds(10))
                        .build()).noInvocationGraph()
                .executable(new Executable() {
                    @Override
                    public void execute() throws Exception {
                        executed.set(true);
                        Thread.sleep(RANDOM.nextInt(10) + 1);
                    }
                });
        final TestBuilder startable = test().noInvocationGraph().startable(
                durationWorker);
        assertFalse(
                "includeInvocationGraph should have been 'false' for MultithreadTestWorkerBuilder!",
                startable.includeInvocationGraph());
        assertFalse(
                "includeInvocationGraph should have been 'false' for MultithreadWorker!",
                durationWorker.includeInvocationGraph());
        startable.start();
        assertTrue(executed.get());
    }

}
