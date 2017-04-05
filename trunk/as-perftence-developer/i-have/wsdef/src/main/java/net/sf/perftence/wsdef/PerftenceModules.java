package net.sf.perftence.wsdef;

import java.util.Arrays;
import java.util.List;

import net.sf.iwant.api.javamodules.JavaBinModule;
import net.sf.iwant.api.javamodules.JavaModule;
import net.sf.iwant.api.javamodules.JavaSrcModule;
import net.sf.iwant.api.model.Path;
import net.sf.iwant.api.model.Source;
import net.sf.iwant.core.download.Downloaded;
import net.sf.iwant.plugin.javamodules.JavaModules;

public class PerftenceModules extends JavaModules {

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

	private final JavaModule völundrBag = JavaBinModule
			.providing(
					Source.underWsroot(
							"völundr-bag/lib/stronglytyped-sortedbag-1.0.3.jar"),
					Source.underWsroot(
							"völundr-bag/lib-sources/stronglytyped-sortedbag-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrConcurrent = JavaBinModule.providing(
			Source.underWsroot("völundr-concurrent/lib/concurrent-1.0.3.jar"),
			Source.underWsroot(
					"völundr-concurrent/lib-sources/concurrent-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrFileutil = JavaBinModule.providing(
			Source.underWsroot("völundr-fileutil/lib/fileutil-1.0.3.jar"),
			Source.underWsroot(
					"völundr-fileutil/lib-sources/fileutil-1.0.3-sources.jar"))
			.end();

	private final JavaModule völundrLinereader = JavaBinModule.providing(
			Source.underWsroot("völundr-linereader/lib/linereader-1.0.3.jar"),
			Source.underWsroot(
					"völundr-linereader/lib-sources/linereader-1.0.3-sources.jar"))
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
			.testDeps(junit).end();

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
					perftenceGraphJfreechart,
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
					perftenceGraphJfreechart,
					reporterfactoryDependenciesJfreechart)
			.end();

	private final JavaSrcModule distributedPerftenceApi = srcModule(
			"distributed-perftence-api").noTestJava()
					.noTestResources()
					.mainDeps(perftence,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							perftenceGraphJfreechart,
							reporterfactoryDependenciesJfreechart, slf4jApi,
							völundrConcurrent)
					.end();

	private final JavaSrcModule defaultPerftenceApiFactory = srcModule(
			"default-perftence-api-factory").noMainResources().noTestJava()
					.noTestResources()
					.mainDeps(perftence, perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph,
							reporterfactoryDependenciesJfreechart)
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
			.testRuntimeDeps(slf4jLog4j12).end();

	private final JavaSrcModule mainentrypointExample = srcModule(
			"mainentrypoint-example").noTestJava()
					.noTestResources()
					.mainDeps(defaultPerftenceApiFactory, perftence,
							perftenceApi,
							perftenceDefaulttestruntimereporterfactory,
							perftenceFluent, perftenceGraph,
							reporterfactoryDependenciesJfreechart, slf4jApi)
					.end();

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
					.end();

	private final JavaSrcModule responsecodeSummaryappender = srcModule(
			"responsecode-summaryappender").noMainResources().noTestResources()
					.mainDeps(perftence, völundrBag).testDeps(junit).end();

	private final JavaSrcModule filebasedReportingProto = srcModule(
			"filebased-reporting-proto")
					.noMainResources()
					.mainDeps(perftence, perftenceAgents,
							perftenceDefaulttestruntimereporterfactory,
							perftenceGraph, perftenceGraphJfreechart,
							reporterfactoryDependenciesJfreechart, slf4jApi,
							völundrLinereader)
					.testDeps(junit, perftenceFluent, perftenceJunit).end();

	private final JavaSrcModule fluentBasedExample = srcModule(
			"fluent-based-example").noMainJava().noMainResources()
					.testDeps(junit, perftence, perftenceFluent, perftenceJunit,
							slf4jApi)
					.end();

	private final JavaSrcModule agentBasedExample = srcModule(
			"agent-based-example").noMainJava().noMainResources()
					.testDeps(junit, perftence, perftenceAgents, perftenceJunit)
					.end();

	// collections

	/**
	 * Basically this just prevents warnings about unused modules
	 */
	List<JavaSrcModule> dependencyRoots() {
		return Arrays.asList(acceptanceTests, agentBasedExample,
				distributedPerftenceApi, filebasedReportingProto,
				fluentBasedExample, mainentrypointExample,
				perftenceExperimental, reporterfactoryDependenciesAfreechart,
				responsecodeSummaryappender);
	}

}
