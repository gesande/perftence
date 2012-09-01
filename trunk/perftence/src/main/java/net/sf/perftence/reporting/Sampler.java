package net.sf.perftence.reporting;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Sampler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sampler.class);

    static List<Integer> sampledValues(final List<Integer> invocations) {
        return sampledValues(invocations, samplingInterval(invocations.size()));
    }

    private static List<Integer> sampledValues(final List<Integer> invocations,
            final int samplesInterval) {
        logTakingSamples(invocations, samplesInterval);
        return takeSamples(invocations, samplesInterval);
    }

    private static void logTakingSamples(final List<Integer> invocations,
            final int samplesInterval) {
        LOGGER.info(
                "Taking samples (sampling interval {}) out the hugish data (size:{}) amount...",
                samplesInterval, invocations.size());
    }

    private static List<Integer> takeSamples(final List<Integer> invocations,
            final int interval) {
        final List<Integer> list = new ArrayList<Integer>();
        int i = 1;
        for (final Integer sample : invocations) {
            if (i % interval == 0) {
                list.add(sample);
            }
            i++;
        }
        return list;
    }

    private static int samplingInterval(final int size) {
        return size / 1000;
    }
}