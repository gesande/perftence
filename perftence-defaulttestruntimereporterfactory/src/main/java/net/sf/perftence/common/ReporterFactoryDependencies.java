package net.sf.perftence.common;

import net.sf.perftence.graph.ImageFactory;
import net.sf.perftence.graph.LineChartAdapterProvider;
import net.sf.perftence.reporting.TestReport;

public interface ReporterFactoryDependencies {

	ThroughputStorageFactory throughputStorageFactory();

	LineChartAdapterProvider<?, ?> lineChartAdapterProvider();

	ImageFactory imageFactory();

	TestReport testReport();

}
