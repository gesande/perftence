package net.sf.perftence.reporting.summary;

//Experimental stuff
public interface SummaryFieldPlugin<FIELDTYPE> {

	public BuildableSummaryField<FIELDTYPE> field();

}
