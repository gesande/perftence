package net.sf.perftence.reporting.summary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SummaryToCsvTest {

    @Test
    public void agentBasedSummaryToCsvTest() {
        String summary = "finished tasks:          200000\n" + "failed tasks:            0\n"
                + "max:                     1392\n" + "average:                 41.42\n"
                + "median:                  40\n" + "95 percentile:           42\n"
                + "throughput:              16110.84\n" + "execution time (ms):     12414\n";
        assertEquals("finished_tasks,failed_tasks,max,average,median,95_percentile,throughput,execution_time_(ms)\n"
                + "200000,0,1392,41.42,40,42,16110.84,12414", SummaryToCsv.convertToCsv(summary).toString());
    }

    @Test
    public void fluentBasedSummaryToCsvTest() {
        String summary = "samples:                 50000/50000\n" + "max:                     100\n"
                + "average:                 45.41\n" + "median:                  45\n" + "95 percentile:           58\n"
                + "throughput:              7908.89\n" + "execution time (ms):     6322\n"
                + "threads:                 400\n";
        assertEquals(
                "samples,max,average,median,95_percentile,throughput,execution_time_(ms),threads\n"
                        + "50000/50000,100,45.41,45,58,7908.89,6322,400",
                SummaryToCsv.convertToCsv(summary).toString());
    }

}
