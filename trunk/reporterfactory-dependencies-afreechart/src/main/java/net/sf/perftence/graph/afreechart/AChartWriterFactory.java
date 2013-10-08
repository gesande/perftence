package net.sf.perftence.graph.afreechart;

import net.sf.perftence.graph.ChartWriter;

import org.afree.chart.AFreeChart;

public interface AChartWriterFactory {

    ChartWriter<AFreeChart> chartWriter(final String reportRootDirectory);

}
