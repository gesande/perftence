package org.fluentjava.perftence.reporting;

public interface ReportingOptions {
    String xAxisTitle();

    String legendTitle();

    String title();

    boolean provideStatistics();

    int range();
}