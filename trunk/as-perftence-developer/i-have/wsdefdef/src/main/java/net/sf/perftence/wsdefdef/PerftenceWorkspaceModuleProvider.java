package net.sf.perftence.wsdefdef;

import net.sf.iwant.api.javamodules.JavaBinModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.wsdef.WorkspaceModuleContext;
import net.sf.iwant.api.wsdef.WorkspaceModuleProvider;
import net.sf.iwant.core.download.FromRepository;

public class PerftenceWorkspaceModuleProvider
		implements WorkspaceModuleProvider {

	public static final JavaBinModule commonsIo = binModule("commons-io",
			"commons-io", "2.4");

	private static JavaBinModule binModule(String group, String name,
			String version) {
		return JavaBinModule.providing(FromRepository.repo1MavenOrg()
				.group(group).name(name).version(version)).end();
	}

	@Override
	public JavaSrcModule workspaceModule(WorkspaceModuleContext ctx) {
		return JavaSrcModule.with().name("perftence-wsdef")
				.locationUnderWsRoot("as-perftence-developer/i-have/wsdef")
				.mainJava("src/main/java").mainDeps(ctx.iwantApiModules())
				.mainDeps(ctx.wsdefdefModule())
				.mainDeps(ctx.iwantPlugin().jacoco().withDependencies())
				.mainDeps(ctx.iwantPlugin().javamodules().withDependencies())
				.mainDeps(commonsIo)
				.end();
	}

	@Override
	public String workspaceFactoryClassname() {
		return "net.sf.perftence.wsdef.PerftenceWorkspaceFactory";
	}

}
