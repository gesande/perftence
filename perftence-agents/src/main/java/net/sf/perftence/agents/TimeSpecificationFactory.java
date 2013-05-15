package net.sf.perftence.agents;

import java.util.concurrent.TimeUnit;

public final class TimeSpecificationFactory {

    private TimeSpecificationFactory() {
    }

    public static Time someMillisecondsFromNow(final int value) {
        return inMillis(value);
    }

    public static Time now() {
        return nanoTime(0);
    }

    private static Time nanoTime(final long time) {
        return newTime(time, TimeUnit.NANOSECONDS);
    }

    public static Time inMillis(final long time) {
        return newTime(time, TimeUnit.MILLISECONDS);
    }

    public static Time inNanos(final long time) {
        return nanoTime(time);
    }

    public static long toNanos(final Time time) {
        return convertTo(TimeUnit.NANOSECONDS, time);
    }

    public static long toMillis(final Time time) {
        return convertTo(TimeUnit.MILLISECONDS, time);
    }

    private static long convertTo(final TimeUnit timeUnit, final Time time) {
        return timeUnit.convert(time.time(), time.timeUnit());
    }

    private static Time newTime(final long time, final TimeUnit timeUnit) {
        return new Time() {
            @Override
            public TimeUnit timeUnit() {
                return timeUnit;
            }

            @Override
            public long time() {
                return time;
            }
        };
    }

}
