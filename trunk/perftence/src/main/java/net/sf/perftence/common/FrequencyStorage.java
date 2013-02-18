package net.sf.perftence.common;

import net.sf.perftence.reporting.graph.ImageData;

public interface FrequencyStorage {

    public boolean hasSamples();

    public ImageData imageData();

}
