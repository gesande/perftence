package net.sf.perftence.reporting;

public final class Duration {
    public final static int SECOND = 1000;
    public final static int MINUTE = 60 * SECOND;
    public final static int HOUR = 60 * MINUTE;
    public final static int DAY = HOUR * 24;

    private Duration() {
    }

    /**
     * @return duration in milliseconds
     */
    public static int hours(final int hours) {
        return HOUR * hours;
    }

    /**
     * @return duration in milliseconds
     */
    public static int seconds(final int seconds) {
        return SECOND * seconds;
    }

    /**
     * @return duration in milliseconds
     */
    public static int minutes(final int minutes) {
        return MINUTE * minutes;
    }

    /**
     * @return duration in milliseconds
     */
    public static int millis(int millis) {
        return millis;
    }
}