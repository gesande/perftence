package net.sf.perftence.reporting.graph;

import java.awt.Paint;

public interface GraphData<DATA, RANGE> {

    Paint paint();

    String title();

    RANGE range();

    DATA range(final RANGE range);

}
