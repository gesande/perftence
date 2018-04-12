package org.fluentjava.perftence.common;

import org.fluentjava.perftence.graph.ImageFactory;
import org.fluentjava.perftence.graph.LineChartAdapterProvider;
import org.fluentjava.perftence.reporting.TestReport;

public interface ReporterFactoryDependencies {

    ThroughputStorageFactory throughputStorageFactory();

    LineChartAdapterProvider<?, ?> lineChartAdapterProvider();

    ImageFactory imageFactory();

    TestReport testReport();

}
