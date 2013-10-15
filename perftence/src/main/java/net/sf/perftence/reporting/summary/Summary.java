package net.sf.perftence.reporting.summary;

public interface Summary<SUMMARY> {

	public Summary<SUMMARY> text(final String text);

	public Summary<SUMMARY> endOfLine();

	public Summary<SUMMARY> bold(final String text);

	public Summary<SUMMARY> note(final String text);
}
