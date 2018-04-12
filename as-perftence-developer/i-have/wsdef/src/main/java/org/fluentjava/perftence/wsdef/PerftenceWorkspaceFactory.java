package org.fluentjava.perftence.wsdef;

import org.fluentjava.iwant.api.wsdef.Workspace;
import org.fluentjava.iwant.api.wsdef.WorkspaceContext;
import org.fluentjava.iwant.api.wsdef.WorkspaceFactory;

public class PerftenceWorkspaceFactory implements WorkspaceFactory {

	@Override
	public Workspace workspace(WorkspaceContext ctx) {
		return new PerftenceWorkspace();
	}

}
