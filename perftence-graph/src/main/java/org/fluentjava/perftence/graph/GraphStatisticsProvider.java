package org.fluentjava.perftence.graph;

public interface GraphStatisticsProvider {

    int median();

    double mean();

    int percentile95();

}
