package net.sf.perftence.wsdefdef;

import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.wsdef.WorkspaceModuleContext;
import net.sf.iwant.api.wsdef.WorkspaceModuleProvider;

public class PerftenceWorkspaceModuleProvider
		implements WorkspaceModuleProvider {

	@Override
	public JavaSrcModule workspaceModule(WorkspaceModuleContext ctx) {
		return JavaSrcModule.with().name("perftence-wsdef")
				.locationUnderWsRoot("as-perftence-developer/i-have/wsdef")
				.mainJava("src/main/java").mainDeps(ctx.iwantApiModules())
				.mainDeps(ctx.wsdefdefModule())
				.mainDeps(ctx.iwantPlugin().jacoco().withDependencies())
				.mainDeps(ctx.iwantPlugin().javamodules().withDependencies())
				.end();
	}

	@Override
	public String workspaceFactoryClassname() {
		return "net.sf.perftence.wsdef.PerftenceWorkspaceFactory";
	}

}
