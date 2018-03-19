package net.sf.perftence.wsdef;

import org.fluentjava.iwant.api.model.Target;

final class RevisionPropertiesBuilder {

	public static Target gitRevision() {
		return new GitRevisionProperties();
	}
}