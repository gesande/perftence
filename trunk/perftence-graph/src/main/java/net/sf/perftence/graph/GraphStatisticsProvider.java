package net.sf.perftence.graph;

public interface GraphStatisticsProvider {

	int median();

	double mean();

	int percentile95();

}
