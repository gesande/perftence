package net.sf.perftence.graph.afreechart;

import org.afree.chart.AFreeChart;

import net.sf.perftence.graph.ChartWriter;

public interface AChartWriterFactory {

    ChartWriter<AFreeChart> chartWriter(final String reportRootDirectory);

}
