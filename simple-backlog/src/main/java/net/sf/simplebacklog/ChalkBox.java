package net.sf.simplebacklog;

public class ChalkBox {

    private static final String PREFIX = "\u001b["; // NOI18N
    private static final String SUFFIX = "m";
    private static final String END = PREFIX + SUFFIX;

    private enum AnsiColor {
        Black, Red, Green, Yellow, Blue, Magenta, Cyan, White;

        public int ansiValue() {
            return ordinal() + 30;
        }
    }

    private static String green(final String text) {
        return new StringBuilder(PREFIX).append(AnsiColor.Green.ansiValue())
                .append(SUFFIX).append(text).append(END).toString();
    }

    private static String red(final String text) {
        return new StringBuilder(PREFIX).append(AnsiColor.Red.ansiValue())
                .append(SUFFIX).append(text).append(END).toString();
    }

    private static String yellow(final String text) {
        return new StringBuilder(PREFIX).append(AnsiColor.Yellow.ansiValue())
                .append(SUFFIX).append(text).append(END).toString();
    }

    @SuppressWarnings("static-method")
    public Chalk green() {
        return Chalks.Green;
    }

    @SuppressWarnings("static-method")
    public Chalk red() {
        return Chalks.Red;
    }

    @SuppressWarnings("static-method")
    public Chalk yellow() {
        return Chalks.Yellow;
    }

    private enum Chalks implements Chalk {
        Green {
            @Override
            public String color(String text) {
                return green(text);
            }
        },
        Red {
            @Override
            public String color(String text) {
                return red(text);
            }
        },
        Yellow {
            @Override
            public String color(String text) {
                return yellow(text);
            }
        };

        @Override
        public abstract String color(String text);
    }

    public interface Chalk {
        String color(String text);
    }

}
