package org.fluentjava.perftence.agents;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.fluentjava.perftence.common.HtmlTestReport;
import org.fluentjava.perftence.graph.jfreechart.DefaultDatasetAdapterFactory;
import org.fluentjava.perftence.graph.jfreechart.ImageFactoryUsingJFreeChart;
import org.fluentjava.perftence.graph.jfreechart.JFreeChartWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.AssertionFailedError;

public class ScheduledExecutorServiceTest {
    private ScheduledExecutorService service;
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledExecutorServiceTest.class);
    private ActiveThreads activeThreads;
    private StorageForThreadsRunningCurrentTasks storage;

    @Test
    public void scheduleJobs1000tasks() {
        scheduleJobs(1000, 1000);
    }

    @Test
    public void scheduleJobs20000tasks() {
        scheduleJobs(1000, 20000);
    }

    @Test
    public void scheduleJobs40000tasks() {
        scheduleJobs(2000, 40000);
    }

    @Test
    public void scheduleJobs60000tasks() {
        scheduleJobs(3000, 60000);
    }

    @Test
    public void scheduleJobs80000tasks() {
        scheduleJobs(4000, 80000);
    }

    @Test
    public void scheduleJobs100000tasks() {
        scheduleJobs(5000, 100000);
    }

    @Test
    public void threadFailingTest() throws InterruptedException {
        final AtomicInteger success = new AtomicInteger();
        final AtomicInteger failed = new AtomicInteger();
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(failTask(failed), 500, TimeUnit.MILLISECONDS);
        executor.schedule(successTask(success), 500, TimeUnit.MILLISECONDS);
        Thread.sleep(2500);
        assertEquals(1, success.intValue());
        assertEquals(1, failed.intValue());
    }

    private static Runnable successTask(final AtomicInteger success) {
        return () -> {
            LOG.info("running success task...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("success task done");
            success.incrementAndGet();

        };
    }

    private static Runnable failTask(final AtomicInteger failed) {
        return () -> {
            try {
                LOG.info("running fail task...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOG.info("fail task 'failed'!");
                failed.incrementAndGet();
                throw new AssertionFailedError("fail task actually failed");
            } catch (Throwable t) {
                LOG.error("Error:", t);
            }
        };
    }

    private void scheduleJobs(final int corePoolSize, final int tasks) {
        this.service = new ScheduledThreadPoolExecutor(corePoolSize);
        this.activeThreads = new ActiveThreads();
        final String name = "ScheduledExecutorServiceTest.scheduleJobs-" + corePoolSize + "-" + tasks;
        this.storage = StorageForThreadsRunningCurrentTasks.newStorage(new DefaultDatasetAdapterFactory());

        log().info("Scheduling tasks...");
        for (int i = 0; i < tasks; i++) {
            this.service.schedule(sleepRunnable(100), 0, TimeUnit.MILLISECONDS);
        }
        log().info("Scheduled all tasks...already active threads {}", this.activeThreads.active());
        try {
            do {
                // nothing
            } while (this.activeThreads.active() > 0);

        } finally {
            this.service.shutdown();
            log().info("Shutdown");
        }
        this.storage.graphWriterFor(name).writeImage(new ImageFactoryUsingJFreeChart(
                new JFreeChartWriter(HtmlTestReport.withDefaultReportPath().reportRootDirectory())));
    }

    private static Logger log() {
        return LOG;
    }

    private Runnable sleepRunnable(final int delay) {
        return () -> {
            ScheduledExecutorServiceTest.this.activeThreads.more();
            ScheduledExecutorServiceTest.this.storage.store(System.nanoTime(),
                    ScheduledExecutorServiceTest.this.activeThreads.active());
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ScheduledExecutorServiceTest.this.activeThreads.less();
            ScheduledExecutorServiceTest.this.storage.store(System.nanoTime(),
                    ScheduledExecutorServiceTest.this.activeThreads.active());
        };
    }
}
