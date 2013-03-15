package net.sf.simplebacklog;

public interface Appender {

    Appender append(final String value);

    Appender newLine();

    Appender tab();

    String build();

}
