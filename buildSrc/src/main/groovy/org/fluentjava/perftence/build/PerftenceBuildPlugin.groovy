package org.fluentjava.perftence.build;

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
            tasks = [
                'perftence:continous',
                'perftence-api:continous',
                'responsecode-summaryappender:continous',
                'perftence-junit:continous',
                'distributed-perftence-api:continous',
                'perftence-fluent:continous',
                'perftence-agents:continous',
                'perftence-graph:continous',
                'perftence-graph-jfreechart:continous',
                'perftence-graph-afreechart:continous',
                'perftence-defaulttestruntimereporterfactory:continous',
                'reporterfactory-dependencies-jfreechart:continous',
                'reporterfactory-dependencies-afreechart:continous',
                'perftence-testreport-html:continous',
                'exportBacklog',
                'exportAntBuildFile',
                'aggregateTestReport',
                'aggregateJDependReport',
                'aggregateFindbugsReport',
                'archiveAggregateReports'
            ]
            doLast { println "Continous build without acceptance tests passed, good work!" }
        }
        project.task("continousBuild", type: GradleBuild) { Task task ->
            group = 'Verification'
            description ='Continous build for the whole thing.'
            buildFile = 'build.gradle'
            tasks = [
                'perftence:continous',
                'perftence-api:continous',
                'responsecode-summaryappender:continous',
                'perftence-junit:continous',
                'distributed-perftence-api:continous',
                'perftence-fluent:continous',
                'perftence-agents:continous',
                'perftence-graph:continous',
                'perftence-graph-jfreechart:continous',
                'perftence-graph-afreechart:continous',
                'perftence-defaulttestruntimereporterfactory:continous',
                'reporterfactory-dependencies-jfreechart:continous',
                'reporterfactory-dependencies-afreechart:continous',
                'perftence-testreport-html:continous',
                'acceptance-tests:test',
                'exportBacklog',
                'exportAntBuildFile',
                'aggregateTestReport',
                'aggregateJDependReport',
                'aggregateFindbugsReport',
                'archiveAggregateReports'
            ]

            doLast { println "Continous build passed, good work!" }
        }
    }
}
