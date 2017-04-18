package net.sf.perftence.wsdef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import net.sf.iwant.api.core.Concatenated;
import net.sf.iwant.api.core.Concatenated.ConcatenatedBuilder;
import net.sf.iwant.api.javamodules.JavaModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.model.Path;
import net.sf.iwant.api.model.SideEffect;
import net.sf.iwant.api.model.Target;
import net.sf.iwant.api.wsdef.SideEffectDefinitionContext;
import net.sf.iwant.api.wsdef.TargetDefinitionContext;
import net.sf.iwant.api.wsdef.Workspace;
import net.sf.iwant.core.download.TestedIwantDependencies;
import net.sf.iwant.eclipsesettings.EclipseSettings;
import net.sf.iwant.plugin.jacoco.JacocoDistribution;
import net.sf.iwant.plugin.jacoco.JacocoTargetsOfJavaModules;

public class PerftenceWorkspace implements Workspace {

	private final PerftenceModules modules = new PerftenceModules();

	@Override
	public List<? extends Target> targets(TargetDefinitionContext ctx) {
		List<Target> t = new ArrayList<>();
		t.add(classdirList());
		t.add(jacocoReportAll());
		t.add(new Distro(modules, new SvnRevisionProperties()));
		return t;
	}

	@Override
	public List<? extends SideEffect> sideEffects(
			SideEffectDefinitionContext ctx) {
		return Arrays.asList(EclipseSettings.with().name("eclipse-settings")
				.modules(ctx.wsdefdefJavaModule(), ctx.wsdefJavaModule())
				.modules(modules.allSrcModules()).end());
	}

	private Target jacocoReportAll() {
		return jacocoReport("jacoco-report-all", modules.allSrcModules());
	}

	private Target jacocoReport(String name,
			SortedSet<JavaSrcModule> interestingModules) {
		return JacocoTargetsOfJavaModules.with()
				.jacocoWithDeps(jacoco(), modules.asmAll.mainArtifact())
				.antJars(TestedIwantDependencies.antJar(),
						TestedIwantDependencies.antLauncherJar())
				.modules(interestingModules).end().jacocoReport(name);

	}

	private static JacocoDistribution jacoco() {
		return JacocoDistribution.newestTestedVersion();
	}

	private Target classdirList() {
		return classdirListOf("classdir-list", modules.allSrcModules(), true);
	}

	private static Target classdirListOf(String name,
			Collection<? extends JavaModule> modules, boolean includeTests) {
		ConcatenatedBuilder classdirs = Concatenated.named(name);
		for (JavaModule module : modules) {
			Path mainClasses = module.mainArtifact();
			if (mainClasses != null) {
				classdirs.unixPathTo(mainClasses).string("\n");
			}
			if (includeTests && module instanceof JavaSrcModule) {
				JavaSrcModule srcMod = (JavaSrcModule) module;
				Path testClasses = srcMod.testArtifact();
				if (testClasses != null) {
					classdirs.unixPathTo(testClasses).string("\n");
				}
			}
		}
		return classdirs.end();
	}

}
