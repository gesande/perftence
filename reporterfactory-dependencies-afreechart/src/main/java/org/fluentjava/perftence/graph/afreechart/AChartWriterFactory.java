package org.fluentjava.perftence.graph.afreechart;

import org.afree.chart.AFreeChart;
import org.fluentjava.perftence.graph.ChartWriter;

public interface AChartWriterFactory {

    ChartWriter<AFreeChart> chartWriter(final String reportRootDirectory);

}
