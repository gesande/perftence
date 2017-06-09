package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.RunNotifier;

public final class DefaultRunNotifier implements RunNotifier {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultRunNotifier.class);

    private List<String> finished = new ArrayList<>();

    @Override
    public void finished(final String id) {
        synchronized (this.finished) {
            this.finished.add(id);
        }
        LOG.info("Done {}", id);
    }

    @Override
    public boolean isFinished(final String id) {
        synchronized (this.finished) {
            return this.finished.contains(id);
        }
    }

}
