package net.sf.perftence.wsdefdef;

import org.fluentjava.iwant.api.javamodules.JavaBinModule;
import org.fluentjava.iwant.api.javamodules.JavaModule;
import org.fluentjava.iwant.api.javamodules.JavaSrcModule;
import org.fluentjava.iwant.api.wsdef.WorkspaceModuleContext;
import org.fluentjava.iwant.api.wsdef.WorkspaceModuleProvider;
import org.fluentjava.iwant.core.javamodules.JavaModules;

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
