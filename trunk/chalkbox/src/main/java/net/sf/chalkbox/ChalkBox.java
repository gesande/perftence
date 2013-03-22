package net.sf.chalkbox;

@SuppressWarnings("static-method")
public final class ChalkBox {

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final String END = PREFIX + SUFFIX;

    private enum AnsiColor {
        Black, Red, Green, Yellow, Blue, Magenta, Cyan, White;

        public int ansiValue() {
            return ordinal() + 30;
        }
    }

    public Chalk black() {
        return Chalks.Black;
    }

    public Chalk red() {
        return Chalks.Red;
    }

    public Chalk green() {
        return Chalks.Green;
    }

    public Chalk yellow() {
        return Chalks.Yellow;
    }

    public Chalk blue() {
        return Chalks.Blue;
    }

    public Chalk magenta() {
        return Chalks.Magenta;
    }

    public Chalk cyan() {
        return Chalks.Cyan;
    }

    public Chalk white() {
        return Chalks.White;
    }

    private enum Chalks implements Chalk {
        Black(AnsiColor.Black), Red(AnsiColor.Red), Green(AnsiColor.Green), Yellow(
                AnsiColor.Yellow), Blue(AnsiColor.Blue), Magenta(
                AnsiColor.Magenta), Cyan(AnsiColor.Cyan), White(AnsiColor.White);

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
