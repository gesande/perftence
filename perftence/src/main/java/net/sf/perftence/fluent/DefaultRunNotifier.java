package net.sf.perftence.fluent;

import java.util.ArrayList;
import java.util.List;

import net.sf.perftence.RunNotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRunNotifier implements RunNotifier {

    private final static Logger LOG = LoggerFactory
            .getLogger(DefaultRunNotifier.class);

    private List<String> finished = new ArrayList<String>();

    @Override
    public void finished(final String id) {
        synchronized (this.finished) {
            this.finished.add(id);
            log().info("Done {}", id);
        }
    }

    private static Logger log() {
        return LOG;
    }

    @Override
    public boolean isFinished(final String id) {
        synchronized (this.finished) {
            return this.finished.contains(id);
        }
    }

}
