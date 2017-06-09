package net.sf.perftence.wsdef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import net.sf.iwant.api.javamodules.CodeFormatterPolicy;
import net.sf.iwant.api.javamodules.CodeFormatterPolicy.TabulationCharValue;
import net.sf.iwant.api.javamodules.CodeStylePolicy;
import net.sf.iwant.api.javamodules.CodeStylePolicy.CodeStylePolicySpex;
import net.sf.iwant.api.javamodules.JavaBinModule;
import net.sf.iwant.api.javamodules.JavaModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.javamodules.JavaSrcModule.IwantSrcModuleSpex;
import net.sf.iwant.api.model.Path;
import net.sf.iwant.api.model.Source;
import net.sf.iwant.core.download.Downloaded;
import net.sf.iwant.plugin.javamodules.JavaModules;

public class PerftenceModules extends JavaModules {

	private static final CodeFormatterPolicy CODE_FORMATTER_POLICY = codeFormatterPolicy();
	private static final CodeStylePolicySpex CODE_STYLE_POLICY = CodeStylePolicy
			.defaultsExcept();

	// bin
	private final Path afreechartJar = Downloaded.withName("afreechartJar")
			.url("https://github.com/gmarques33/repos/raw/master/releases/org/afree/afreechart/"
					+ "0.0.4/afreechart-0.0.4.jar")
			.md5("cdca9ce40b95c104f44e624dc6ee29c2");
	private final JavaModule afreechart = JavaBinModule.providing(afreechartJar)
			.end();

	final JavaBinModule asmAll = binModule("org.ow2.asm", "asm-all", "5.0.1");

	private final JavaModule commonsCollections = binModule(
			"commons-collections", "commons-collections", "3.2.1");

	private final JavaModule jcommon = binModule("jfree", "jcommon", "1.0.15");

	private final JavaModule jfreechart = binModule("jfree", "jfreechart",
			"1.0.12");

	private final JavaModule junit = binModule("junit", "junit", "4.10");

	private final JavaModule log4j = binModule("log4j", "log4j", "1.2.16");

	private final JavaModule slf4jApi = binModule("org.slf4j", "slf4j-api",
			"1.6.1", log4j);

	private final JavaModule slf4jLog4j12 = binModule("org.slf4j",
			"slf4j-log4j12", "1.6.1");

	private final JavaModule völundrBag = JavaBinModule.providing(
			Source.underWsroot("lib-repo/stronglytyped-sortedbag-1.0.3.jar"),
			Source.underWsroot(
					"lib-repo/sources/stronglytyped-sortedbag-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrConcurrent = JavaBinModule
			.providing(Source.underWsroot("lib-repo/concurrent-1.0.3.jar"),
					Source.underWsroot(
							"lib-repo/sources/concurrent-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrFileutil = JavaBinModule
			.providing(Source.underWsroot("lib-repo/fileutil-1.0.3.jar"),
					Source.underWsroot(
							"lib-repo/sources/fileutil-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrLinereader = JavaBinModule
			.providing(Source.underWsroot("lib-repo/linereader-1.0.3.jar"),
					Source.underWsroot(
							"lib-repo/sources/linereader-1.0.3-sources.jar"))
			.end();

	// src

	private final JavaSrcModule perftenceGraph = srcModuleWithDefaults(
			"perftence-graph").noTestJava().noTestResources().end();

	private final JavaSrcModule perftenceGraphAfreechart = srcModuleWithDefaults(
			"perftence-graph-afreechart").noTestJava().noTestResources()
					.mainDeps(afreechart, perftenceGraph, slf4jApi).end();

	private final JavaSrcModule perftenceGraphJfreechart = srcModuleWithDefaults(
			"perftence-graph-jfreechart").noTestJava().noTestResources()
					.mainDeps(jcommon, jfreechart, perftenceGraph, slf4jApi,
							völundrFileutil)
					.end();

	private final JavaSrcModule perftence = srcModuleWithDefaults("perftence")
			.mainDeps(commonsCollections, perftenceGraph, völundrBag, slf4jApi)
			.testDeps(junit, slf4jLog4j12, log4j).end();

	private final JavaSrcModule perftenceTestreportHtml = srcModuleWithDefaults(
			"perftence-testreport-html").noMainResources().noTestJava()
					.noTestResources()
					.mainDeps(perftence, slf4jApi, völundrFileutil).end();

	private final JavaSrcModule perftenceDefaulttestruntimereporterfactory = srcModuleWithDefaults(
			"perftence-defaulttestruntimereporterfactory").noTestResources()
					.mainDeps(perftence, perftenceGraph, slf4jApi)
					.testDeps(junit, perftenceGraphJfreechart,
							perftenceTestreportHtml)
					.end();

	private final JavaSrcModule reporterfactoryDependenciesJfreechart = srcModuleWithDefaults(
			"reporterfactory-dependencies-jfreechart").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphJfreechart,
							perftenceTestreportHtml)
					.end();

	private final JavaSrcModule reporterfactoryDependenciesAfreechart = srcModuleWithDefaults(
			"reporterfactory-dependencies-afreechart").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphAfreechart,
							perftenceTestreportHtml, slf4jApi, afreechart,
							völundrFileutil)
					.end();

	private final JavaSrcModule perftenceFluent = srcModuleWithDefaults(
			"perftence-fluent")
					.noTestResources()
					.mainDeps(perftence, perftenceGraph, slf4jApi,
							völundrConcurrent)
					.testDeps(junit, perftenceDefaulttestruntimereporterfactory,
							perftenceGraphJfreechart, perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart)
					.end();

	private final JavaSrcModule perftenceAgents = srcModuleWithDefaults(
			"perftence-agents")
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, slf4jApi, völundrBag)
					.testDeps(jfreechart, junit, perftenceGraphJfreechart,
							perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart)
					.end();

	private final JavaSrcModule perftenceApi = srcModuleWithDefaults(
			"perftence-api")
					.noTestResources()
					.mainDeps(perftence, perftenceAgents, perftenceFluent,
							perftenceGraph)
					.testDeps(junit, perftenceDefaulttestruntimereporterfactory,
							perftenceGraphJfreechart, perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart)
					.end();

	private final JavaSrcModule distributedPerftenceApi = srcModuleWithDefaults(
			"distributed-perftence-api").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							perftenceGraphJfreechart, perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart, slf4jApi,
							völundrConcurrent)
					.end();

	private final JavaSrcModule defaultPerftenceApiFactory = srcModuleWithDefaults(
			"default-perftence-api-factory").noMainResources().noTestJava()
					.noTestResources()
					.mainDeps(perftence, perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph,
							reporterfactoryDependenciesJfreechart,
							perftenceTestreportHtml, völundrFileutil)
					.end();

	private final JavaSrcModule perftenceJunit = srcModuleWithDefaults(
			"perftence-junit")
					.noTestResources()
					.mainDeps(defaultPerftenceApiFactory, junit, perftence,
							perftenceAgents, perftenceApi, perftenceFluent,
							slf4jApi)
					.end();

	private final JavaSrcModule acceptanceTests = srcModuleWithDefaults(
			"acceptance-tests").noMainJava()
					.noTestResources()
					.testDeps(junit, perftence, perftenceAgents,
							perftenceFluent, perftenceJunit, perftenceGraph,
							perftenceGraphJfreechart, perftenceTestreportHtml,
							slf4jApi)
					.testRuntimeDeps(slf4jLog4j12, log4j).end();

	private final JavaSrcModule mainentrypointExample = srcModuleWithDefaults(
			"mainentrypoint-example").noTestJava()
					.noTestResources()
					.mainDeps(defaultPerftenceApiFactory, log4j, perftence,
							perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart, slf4jApi)
					.mainRuntimeDeps(log4j, slf4jLog4j12).end();

	private final JavaSrcModule perftenceExperimental = srcModuleWithDefaults(
			"perftence-experimental").noMainResources()
					.noTestResources()
					.mainDeps(distributedPerftenceApi, perftence,
							perftenceAgents, perftenceFluent, slf4jApi,
							völundrConcurrent)
					.testDeps(commonsCollections, junit,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphJfreechart,
							perftenceJunit, perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart)
					.testRuntimeDeps(slf4jLog4j12, log4j).end();

	private final JavaSrcModule responsecodeSummaryappender = srcModuleWithDefaults(
			"responsecode-summaryappender").noMainResources().noTestResources()
					.mainDeps(perftence, völundrBag).testDeps(junit).end();

	private final JavaSrcModule filebasedReportingProto = srcModuleWithDefaults(
			"filebased-reporting-proto")
					.noMainResources()
					.mainDeps(perftence, perftenceAgents,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphJfreechart,
							perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart, slf4jApi,
							völundrLinereader)
					.testDeps(junit, perftenceFluent, perftenceJunit)
					.testRuntimeDeps(log4j, slf4jLog4j12).end();

	private final JavaSrcModule fluentBasedExample = srcModuleWithDefaults(
			"fluent-based-example").noMainJava().noMainResources()
					.testDeps(junit, perftence, perftenceFluent, perftenceJunit,
							slf4jApi)
					.end();

	private final JavaSrcModule agentBasedExample = srcModuleWithDefaults(
			"agent-based-example").noMainJava().noMainResources()
					.testDeps(junit, log4j, perftence, perftenceAgents,
							perftenceJunit, slf4jApi, slf4jLog4j12)
					.end();

	// collections
	List<JavaSrcModule> productionDependencyRoots() {
		return Arrays.asList(distributedPerftenceApi, perftence,
				perftenceAgents, perftenceApi,
				perftenceDefaulttestruntimereporterfactory, perftenceFluent,
				perftenceGraph, perftenceGraphAfreechart,
				perftenceGraphJfreechart, perftenceJunit,
				perftenceTestreportHtml, reporterfactoryDependenciesAfreechart,
				reporterfactoryDependenciesJfreechart,
				responsecodeSummaryappender);
	}

	private IwantSrcModuleSpex srcModuleWithDefaults(String string) {
		return srcModule(string).codeFormatter(CODE_FORMATTER_POLICY)
				.codeStyle(CODE_STYLE_POLICY.end());
	}

	private static CodeFormatterPolicy codeFormatterPolicy() {
		CodeFormatterPolicy codeFormatterPolicy = new CodeFormatterPolicy();
		codeFormatterPolicy.lineSplit = 120;
		codeFormatterPolicy.tabulationChar = TabulationCharValue.SPACE;
		return codeFormatterPolicy;
	}

	/**
	 * Basically this just prevents warnings about unused modules
	 */
	List<JavaSrcModule> dependencyRoots() {
		List<JavaSrcModule> roots = new ArrayList<>();
		roots.addAll(productionDependencyRoots());
		roots.add(mainentrypointExample);
		roots.add(perftenceExperimental);
		roots.add(acceptanceTests);
		roots.add(agentBasedExample);
		roots.add(fluentBasedExample);
		roots.add(filebasedReportingProto);
		return roots;
	}

	public SortedSet<JavaSrcModule> modulesForJacoco() {
		SortedSet<JavaSrcModule> modulesForJacoco = new TreeSet<>(
				allSrcModules());
		modulesForJacoco.removeIf(new Predicate<JavaSrcModule>() {
			@Override
			public boolean test(JavaSrcModule module) {
				return module.equals(perftenceExperimental)
						|| module.equals(filebasedReportingProto)
						|| module.equals(mainentrypointExample)
						|| module.equals(fluentBasedExample)
						|| module.equals(agentBasedExample)
						|| module.equals(distributedPerftenceApi);
			}
		});
		return modulesForJacoco;
	}

}
