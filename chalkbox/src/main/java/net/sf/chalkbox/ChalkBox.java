package net.sf.chalkbox;

public final class ChalkBox {

    private static final String PREFIX = "\u001b["; // NOI18N
    private static final String SUFFIX = "m";
    private static final String END = PREFIX + SUFFIX;

    private enum AnsiColor {
        Black, Red, Green, Yellow, Blue, Magenta, Cyan, White;

        public int ansiValue() {
            return ordinal() + 30;
        }
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
        Green(AnsiColor.Green), Red(AnsiColor.Red), Yellow(AnsiColor.Yellow);

        private AnsiColor color;

        private Chalks(final AnsiColor color) {
            this.color = color;
        }

        @Override
        public String write(final String text) {
            return new StringBuilder(PREFIX).append(ansiColor().ansiValue())
                    .append(SUFFIX).append(text).append(END).toString();
        }

        private AnsiColor ansiColor() {
            return this.color;
        }
    }

}
