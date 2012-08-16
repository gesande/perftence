package net.sf.perftence.reporting.graph;

import java.awt.Paint;

import org.jfree.data.Range;

public interface GraphData<T> {

    Paint paint();

    String title();

    Range range();

    T range(Range range);

}
