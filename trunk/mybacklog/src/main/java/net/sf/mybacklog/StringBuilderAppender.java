package net.sf.mybacklog;

public final class StringBuilderAppender implements Appender {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public Appender append(String value) {
        appender().append(value);
        return this;
    }

    @Override
    public Appender newLine() {
        append(newLineCharacters());
        return this;
    }

    @Override
    public Appender tab() {
        append(tabCharacters());
        return this;
    }

    private StringBuilder appender() {
        return this.sb;
    }

    private static String newLineCharacters() {
        return "\n";
    }

    private static String tabCharacters() {
        return "  ";
    }

    @Override
    public String build() {
        return appender().toString();
    }
}
