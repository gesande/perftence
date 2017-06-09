package net.sf.perftence.wsdef;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import net.sf.iwant.api.javamodules.CodeFormatterPolicy;
import net.sf.iwant.api.javamodules.CodeFormatterPolicy.TabulationCharValue;
import net.sf.iwant.api.javamodules.CodeStyle;
import net.sf.iwant.api.javamodules.CodeStylePolicy;
import net.sf.iwant.api.javamodules.JavaBinModule;
import net.sf.iwant.api.javamodules.JavaCompliance;
import net.sf.iwant.api.javamodules.JavaModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.javamodules.JavaSrcModule.IwantSrcModuleSpex;
import net.sf.iwant.api.model.Path;
import net.sf.iwant.api.model.Source;
import net.sf.iwant.core.download.Downloaded;
import net.sf.iwant.plugin.javamodules.JavaModules;

public class PerftenceModules extends JavaModules {

	private static final CodeFormatterPolicy CODE_FORMATTER_POLICY = codeFormatterPolicy();

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

	private final JavaSrcModule perftenceGraph = srcModule("perftence-graph")
			.noTestJava().noTestResources().end();

	private final JavaSrcModule perftenceGraphAfreechart = srcModule(
			"perftence-graph-afreechart").noTestJava().noTestResources()
					.mainDeps(afreechart, perftenceGraph, slf4jApi).end();

	private final JavaSrcModule perftenceGraphJfreechart = srcModule(
			"perftence-graph-jfreechart").noTestJava().noTestResources()
					.mainDeps(jcommon, jfreechart, perftenceGraph, slf4jApi,
							völundrFileutil)
					.end();

	private final JavaSrcModule perftence = srcModule("perftence")
			.mainDeps(commonsCollections, perftenceGraph, völundrBag, slf4jApi)
			.testDeps(junit, slf4jLog4j12, log4j).end();

	private final JavaSrcModule perftenceTestreportHtml = srcModule(
			"perftence-testreport-html").noMainResources().noTestJava()
					.noTestResources()
					.mainDeps(perftence, slf4jApi, völundrFileutil).end();

	private final JavaSrcModule perftenceDefaulttestruntimereporterfactory = srcModule(
			"perftence-defaulttestruntimereporterfactory").noTestResources()
					.mainDeps(perftence, perftenceGraph, slf4jApi)
					.testDeps(junit, perftenceGraphJfreechart,
							perftenceTestreportHtml)
					.end();

	private final JavaSrcModule reporterfactoryDependenciesJfreechart = srcModule(
			"reporterfactory-dependencies-jfreechart").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphJfreechart,
							perftenceTestreportHtml)
					.end();

	private final JavaSrcModule reporterfactoryDependenciesAfreechart = srcModule(
			"reporterfactory-dependencies-afreechart").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphAfreechart,
							perftenceTestreportHtml, slf4jApi, afreechart,
							völundrFileutil)
					.end();

	private final JavaSrcModule perftenceFluent = srcModule("perftence-fluent")
			.noTestResources()
			.mainDeps(perftence, perftenceGraph, slf4jApi, völundrConcurrent)
			.testDeps(junit, perftenceDefaulttestruntimereporterfactory,
					perftenceGraphJfreechart, perftenceTestreportHtml,
					reporterfactoryDependenciesJfreechart)
			.end();

	private final JavaSrcModule perftenceAgents = srcModule("perftence-agents")
			.noTestResources()
			.mainDeps(perftence, perftenceDefaulttestruntimereporterfactory,
					perftenceGraph, slf4jApi, völundrBag)
			.testDeps(jfreechart, junit, perftenceGraphJfreechart,
					perftenceTestreportHtml,
					reporterfactoryDependenciesJfreechart)
			.end();

	private final JavaSrcModule perftenceApi = srcModule("perftence-api")
			.noTestResources()
			.mainDeps(perftence, perftenceAgents, perftenceFluent,
					perftenceGraph)
			.testDeps(junit, perftenceDefaulttestruntimereporterfactory,
					perftenceGraphJfreechart, perftenceTestreportHtml,
					reporterfactoryDependenciesJfreechart)
			.end();

	private final JavaSrcModule distributedPerftenceApi = srcModule(
			"distributed-perftence-api").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							perftenceGraphJfreechart, perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart, slf4jApi,
							völundrConcurrent)
					.end();

	private final JavaSrcModule defaultPerftenceApiFactory = srcModule(
			"default-perftence-api-factory").noMainResources().noTestJava()
					.noTestResources()
					.mainDeps(perftence, perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph,
							reporterfactoryDependenciesJfreechart,
							perftenceTestreportHtml, völundrFileutil)
					.end();

	private final JavaSrcModule perftenceJunit = srcModule("perftence-junit")
			.noTestResources()
			.mainDeps(defaultPerftenceApiFactory, junit, perftence,
					perftenceAgents, perftenceApi, perftenceFluent, slf4jApi)
			.end();

	private final JavaSrcModule acceptanceTests = srcModule("acceptance-tests")
			.noMainJava().noTestResources()
			.testDeps(junit, perftence, perftenceAgents, perftenceFluent,
					perftenceJunit, perftenceGraph, perftenceGraphJfreechart,
					perftenceTestreportHtml, slf4jApi)
			.testRuntimeDeps(slf4jLog4j12, log4j).end();

	private final JavaSrcModule mainentrypointExample = srcModule(
			"mainentrypoint-example").noTestJava()
					.noTestResources()
					.mainDeps(defaultPerftenceApiFactory, log4j, perftence,
							perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							perftenceTestreportHtml,
							reporterfactoryDependenciesJfreechart, slf4jApi)
					.mainRuntimeDeps(log4j, slf4jLog4j12).end();

	private final JavaSrcModule perftenceExperimental = srcModule(
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

	private final JavaSrcModule responsecodeSummaryappender = srcModule(
			"responsecode-summaryappender").noMainResources().noTestResources()
					.mainDeps(perftence, völundrBag).testDeps(junit).end();

	private final JavaSrcModule filebasedReportingProto = srcModule(
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

	private final JavaSrcModule fluentBasedExample = srcModule(
			"fluent-based-example").noMainJava().noMainResources()
					.testDeps(junit, perftence, perftenceFluent, perftenceJunit,
							slf4jApi)
					.end();

	private final JavaSrcModule agentBasedExample = srcModule(
			"agent-based-example").noMainJava().noMainResources()
					.testDeps(junit, log4j, perftence, perftenceAgents,
							perftenceJunit, slf4jApi, slf4jLog4j12)
					.end();

	// override common settings, like code formatter and code style

	@Override
	protected IwantSrcModuleSpex commonSettings(IwantSrcModuleSpex m) {
		return super.commonSettings(m).encoding(Charset.forName("UTF-8"))
				.codeFormatter(CODE_FORMATTER_POLICY)
				.codeStyle(CodeStylePolicy.defaultsExcept()
						.warn(CodeStyle.MISSING_DEFAULT_CASE)
						.warn(CodeStyle.REDUNDANT_SUPERINTERFACE)
						.warn(CodeStyle.UNUSED_TYPE_PARAMETER)
						.warn(CodeStyle.UNQUALIFIED_FIELD_ACCESS)
						.warn(CodeStyle.UNNECESSARY_ELSE)
						.warn(CodeStyle.POTENTIALLY_UNCLOSED_CLOSEABLE)

						.end())
				.javaCompliance(JavaCompliance.JAVA_1_8);
	}

	private static CodeFormatterPolicy codeFormatterPolicy() {
		CodeFormatterPolicy codeFormatterPolicy = new CodeFormatterPolicy();
		codeFormatterPolicy.lineSplit = 120;
		codeFormatterPolicy.tabulationChar = TabulationCharValue.SPACE;
		return codeFormatterPolicy;
	}

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
