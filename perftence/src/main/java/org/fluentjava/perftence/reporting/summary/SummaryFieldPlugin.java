package org.fluentjava.perftence.reporting.summary;

//Experimental stuff
public interface SummaryFieldPlugin<FIELDTYPE> {

    public BuildableSummaryField<FIELDTYPE> field();

}
