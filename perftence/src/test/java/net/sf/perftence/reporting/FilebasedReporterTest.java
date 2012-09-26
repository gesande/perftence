package net.sf.perftence.reporting;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class FilebasedReporterTest extends AbstractMultiThreadedTest {

    @Test
    public void write() {
        final AtomicInteger i = new AtomicInteger();
        final Random r = new Random(System.currentTimeMillis());
        final FilebasedReporter reporter = new FilebasedReporter(id());
        test().setup(setup().threads(100).invocations(10000).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        int value = i.incrementAndGet();
                        reporter.latency(value);
                        reporter.throughput(value, r.nextInt(100) + 1);

                    }
                }).start();

        reporter.summary("id", 1000, 10000, 0);
    }
}
