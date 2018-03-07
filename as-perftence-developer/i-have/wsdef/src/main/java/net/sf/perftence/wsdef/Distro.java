package net.sf.perftence.wsdef;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import org.fluentjava.iwant.api.core.Concatenated;
import org.fluentjava.iwant.api.core.Concatenated.ConcatenatedBuilder;
import org.fluentjava.iwant.api.core.ScriptGenerated;
import org.fluentjava.iwant.api.javamodules.JavaModule;
import org.fluentjava.iwant.api.model.Path;
import org.fluentjava.iwant.api.model.Source;
import org.fluentjava.iwant.api.model.TargetEvaluationContext;
import org.fluentjava.iwant.api.target.TargetBase;
import org.fluentjava.iwant.core.javamodules.JavaModules;

class Distro extends TargetBase {

	private static final String majorVer = "2.1.4";
	private final SvnRevisionProperties svnRevisionProperties;
	private final Map<JavaModule, Path> jars = new LinkedHashMap<>();
	private final List<Path> zips;
	private final List<Path> srcJars;
	private Path copying;

	public Distro(PerftenceModules modules,
			SvnRevisionProperties svnRevisionProperties) {
		super("perftence-distribution");
		this.svnRevisionProperties = svnRevisionProperties;
		zips = modules.productionDependencyRoots().stream()
				.map(m -> zippedRuntimeJarsOf(m)).collect(Collectors.toList());
		srcJars = modules.allSrcModules().stream()
				.map(m -> JavaModules.srcJarOf(majorVer, m))
				.filter(s -> s != null).collect(Collectors.toList());
		copying = Source.underWsroot("COPYING");
	}

	private Path zippedRuntimeJarsOf(JavaModule depRoot) {
		String name = depRoot.name() + "-" + majorVer + ".zip";
		ConcatenatedBuilder sh = Concatenated.named(name + ".sh");
		sh.string("#!/bin/bash\n");
		sh.string("set -eu\n");
		sh.string("DEST=$1\n");
		sh.string("zip -jq \"$DEST\"");
		for (Path jar : runtimeJarsOf(depRoot)) {
			sh.string(" '").unixPathTo(jar).string("'");
		}
		sh.string("\n");
		return ScriptGenerated.named(name).byScript(sh.end());
	}

	private List<Path> runtimeJarsOf(JavaModule depRoot) {
		Set<JavaModule> modules = JavaModules.runtimeDepsOf(depRoot);
		return modules.stream().map(m -> jarOf(m)).collect(Collectors.toList());
	}

	private Path jarOf(JavaModule module) {
		Path jar = jars.get(module);
		if (jar == null) {
			jar = JavaModules.mainJarOf(majorVer, module);
			jars.put(module, jar);
		}
		return jar;
	}

	@Override
	protected IngredientsAndParametersDefined ingredientsAndParameters(
			IngredientsAndParametersPlease iUse) {
		return iUse.parameter("majorVer", majorVer).ingredients("me",
				Source.underWsroot("as-perftence-developer/i-have/wsdef/"
						+ "src/main/java/"
						+ "net/sf/perftence/wsdef/Distro.java"))
				.ingredients("svnRevisionProperties", svnRevisionProperties)
				.ingredients("COPYING", copying).ingredients("zips", zips)
				.ingredients("srcJars", srcJars).nothingElse();
	}

	@Override
	public void path(TargetEvaluationContext ctx) throws Exception {
		Properties p = new Properties();
		try (InputStream in = new FileInputStream(
				ctx.cached(svnRevisionProperties))) {
			p.load(in);
		}
		String rev = p.getProperty("Revision");

		File distDir = new File(ctx.cached(this), name() + "-R" + rev);
		System.err.println("Populating " + distDir);
		FileUtils.forceMkdir(distDir);

		FileUtils.copyFileToDirectory(ctx.cached(copying), distDir);

		for (Path zip : zips) {
			FileUtils.copyFileToDirectory(ctx.cached(zip), distDir);
		}

		File sources = new File(distDir, "sources");
		FileUtils.forceMkdir(sources);
		for (Path srcJar : srcJars) {
			FileUtils.copyFileToDirectory(ctx.cached(srcJar), sources);
		}

		System.err.println("Done populating " + distDir);
	}

}
