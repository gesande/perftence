package net.sf.perftence.fluent;

final class EstimatedInvocations {

	@SuppressWarnings("static-method")
	public long calculate(final double currentThroughput, final int duration,
			final long sampleCount) {
		return Math.max((long) (duration * currentThroughput / 1000),
				sampleCount);
	}
}
