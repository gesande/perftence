package org.fluentjava.perftence;

public final class AllowedExceptionOrErrorOccurredMessageBuilder {

    @SuppressWarnings("static-method")
    public String allowedExceptionOccurredMessage(final Exception cause, final String testName) {
        return new StringBuilder("Allowed exception ").append(cause.getClass().getSimpleName())
                .append(" occurred while running the test: ").append(testName).toString();
    }

    @SuppressWarnings("static-method")
    public String allowedErrorOccurredMessage(Error cause, String testName) {
        return new StringBuilder("Allowed error ").append(cause.getClass().getSimpleName())
                .append(" occurred while running the test: ").append(testName).toString();
    }

}
