package net.sf.perftence.graph;

import java.awt.Paint;

public interface GraphData<DATA, RANGE> {

    Paint paint();

    String title();

    RANGE range();

    DATA range(final RANGE range);

}
