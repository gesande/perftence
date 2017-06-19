package net.sf.perftence.wsdefdef;

import net.sf.iwant.api.javamodules.JavaBinModule;
import net.sf.iwant.api.javamodules.JavaModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.wsdef.WorkspaceModuleContext;
import net.sf.iwant.api.wsdef.WorkspaceModuleProvider;
import net.sf.iwant.core.javamodules.JavaModules;

public class PerftenceWorkspaceModuleProvider
		implements WorkspaceModuleProvider {

	public static final JavaBinModule commonsIo = binModule("commons-io",
			"commons-io", "2.4");

	private static JavaBinModule binModule(String group, String name,
			String version, JavaModule... runtimeDeps) {
		return JavaModules.binModule(group, name, version, runtimeDeps);
	}

	@Override
	public JavaSrcModule workspaceModule(WorkspaceModuleContext ctx) {
		return JavaSrcModule.with().name("perftence-wsdef")
				.locationUnderWsRoot("as-perftence-developer/i-have/wsdef")
				.mainJava("src/main/java").mainDeps(ctx.iwantApiModules())
				.mainDeps(ctx.wsdefdefModule())
				.mainDeps(ctx.iwantPlugin().jacoco().withDependencies())
				.mainDeps(commonsIo).end();
	}

	@Override
	public String workspaceFactoryClassname() {
		return "net.sf.perftence.wsdef.PerftenceWorkspaceFactory";
	}

}
