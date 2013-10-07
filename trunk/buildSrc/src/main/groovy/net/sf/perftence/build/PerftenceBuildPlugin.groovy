package net.sf.perftence.build;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.GradleBuild

public class PerftenceBuildPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.task("licenseToCommit", type: GradleBuild) { Task task ->
            group = 'Verification'
            description ='If this passed you have license to commit your changes.'
            buildFile = 'build.gradle'
           
            tasks << 'perftence:continous'
            tasks << 'perftence-api:continous'
            tasks << 'responsecode-summaryappender:continous'
            tasks << 'perftence-junit:continous'
            tasks << 'distributed-perftence-api:continous'
            tasks << 'perftence-fluent:continous'
            tasks << 'perftence-agents:continous'
            tasks << 'perftence-graph:continous'
            tasks << 'perftence-graph-jfreechart:continous'
            tasks << 'perftence-defaulttestruntimereporterfactory:continous'
            tasks << 'reporterfactory-dependencies-jfreechart:continous'
            
            tasks << 'exportBacklog'
            tasks << 'exportAntBuildFile'
            tasks << 'aggregateTestReport'
            tasks << 'aggregateJDependReport'
            tasks << 'aggregateCoverageReport'
            tasks << 'aggregateFindbugsReport'
            tasks << 'archiveAggregateReports'

            doLast { println "Continous build without acceptance tests passed, good work!" }
        }
        project.task("continousBuild", type: GradleBuild) { Task task ->
            group = 'Verification'
            description ='Continous build for the whole thing.'
            buildFile = 'build.gradle'
           
            tasks << 'perftence:continous'
            tasks << 'perftence-api:continous'
            tasks << 'responsecode-summaryappender:continous'
            tasks << 'perftence-junit:continous'
            tasks << 'distributed-perftence-api:continous'
            tasks << 'perftence-fluent:continous'
            tasks << 'perftence-agents:continous'
            tasks << 'perftence-graph:continous'
            tasks << 'perftence-graph-jfreechart:continous'
            tasks << 'perftence-defaulttestruntimereporterfactory:continous'
            tasks << 'reporterfactory-dependencies-jfreechart:continous'
            
            tasks << 'acceptance-tests:test'

            tasks << 'exportBacklog'
            tasks << 'exportAntBuildFile'
            tasks << 'aggregateTestReport'
            tasks << 'aggregateJDependReport'
            tasks << 'aggregateCoverageReport'
            tasks << 'aggregateFindbugsReport'
            tasks << 'archiveAggregateReports'

            doLast { println "Continous build passed, good work!" }
        }
        project.task("distributionPackage", type: GradleBuild, dependsOn: ['continousBuild']) { Task task ->
            group = 'Distribution'
            description = 'Distribution package for the whole thing including continous build.'
            buildFile = 'build.gradle'

            tasks << 'perftence-api:clean'
            tasks << 'perftence:clean'
            tasks << 'responsecode-summaryappender:clean'
            tasks << 'perftence-junit:clean'
            tasks << 'distributed-perftence-api:clean'
            tasks << 'perftence-fluent:clean'
            tasks << 'perftence-agents:clean'
            tasks << 'perftence-graph:clean'
            tasks << 'perftence-graph-jfreechart:clean'
            tasks << 'perftence-defaulttestruntimereporterfactory:clean'
            tasks << 'reporterfactory-dependencies-jfreechart:clean'
            
            tasks << 'perftence-api:release'
            tasks << 'perftence:release'
            tasks << 'responsecode-summaryappender:release'
            tasks << 'perftence-junit:release'
            tasks << 'distributed-perftence-api:release'
            tasks << 'perftence-fluent:release'
            tasks << 'perftence-agents:release'
            tasks << 'perftence-graph:release'
            tasks << 'perftence-graph-jfreechart:release'
            tasks << 'perftence-defaulttestruntimereporterfactory:release'
            tasks << 'reporterfactory-dependencies-jfreechart:release'
            
            tasks << 'makeDistributionPackage'

            doLast { println "Distribution package ready to be uploaded to the repository." }
        }
    }
}
