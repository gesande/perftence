package net.sf.perftence;

public interface RunNotifier {
	void finished(final String id);

	boolean isFinished(final String id);
}