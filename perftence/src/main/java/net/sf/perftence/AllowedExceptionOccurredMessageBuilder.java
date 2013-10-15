package net.sf.perftence;

public final class AllowedExceptionOccurredMessageBuilder {

	@SuppressWarnings("static-method")
	public String allowedExceptionOccurredMessage(final Throwable t,
			final String testName) {
		return new StringBuilder("Allowed exception ")
				.append(t.getClass().getSimpleName())
				.append(" occurred while running the test: ").append(testName)
				.toString();
	}

}
