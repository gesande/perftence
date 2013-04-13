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

            tasks << 'mybacklog:clean'
            tasks << 'perftence-concurrent:clean'
            tasks << 'perftence-bag:clean'
            tasks << 'perftence-linereader:clean'
            tasks << 'perftence-fileutil:clean'
            tasks << 'perftence-junit-utils:clean'
            tasks << 'perftence-classhelper:clean'
            tasks << 'perftence:clean'
            tasks << 'perftence-api:clean'
            tasks << 'responsecode-summaryappender:clean'
            tasks << 'perftence-junit:clean'

            tasks << 'mybacklog:continous'
            tasks << 'perftence-concurrent:continous'
            tasks << 'perftence-bag:continous'
            tasks << 'perftence-linereader:continous'
            tasks << 'perftence-fileutil:continous'
            tasks << 'perftence-junit-utils:continous'
            tasks << 'perftence-classhelper:continous'
            tasks << 'perftence:continous'
            tasks << 'perftence-api:continous'
            tasks << 'responsecode-summaryappender:continous'
            tasks << 'perftence-junit:continous'

            tasks << 'exportBacklog'

            tasks << 'aggregateTestReport'
            tasks << 'aggregateJDependReport'
            tasks << 'aggregateCoverageReport'
            tasks << 'aggregateFindbugsReport'

            doLast { println "Continous build without acceptance tests passed, good work!" }
        }
        project.task("continousBuild", type: GradleBuild) { Task task ->
            group = 'Verification'
            description ='Continous build for the whole thing.'
            buildFile = 'build.gradle'

            tasks << 'mybacklog:clean'
            tasks << 'perftence-concurrent:clean'
            tasks << 'perftence-bag:clean'
            tasks << 'perftence-linereader:clean'
            tasks << 'perftence-fileutil:clean'
            tasks << 'perftence-junit-utils:clean'
            tasks << 'perftence-classhelper:clean'
            tasks << 'perftence:clean'
            tasks << 'perftence-api:clean'
            tasks << 'responsecode-summaryappender:clean'
            tasks << 'perftence-junit:clean'
            tasks << 'mybacklog:continous'
            tasks << 'perftence-concurrent:continous'
            tasks << 'perftence-bag:continous'
            tasks << 'perftence-linereader:continous'
            tasks << 'perftence-fileutil:continous'
            tasks << 'perftence-junit-utils:continous'
            tasks << 'perftence-classhelper:continous'
            tasks << 'perftence:continous'
            tasks << 'perftence-api:continous'
            tasks << 'responsecode-summaryappender:continous'
            tasks << 'perftence-junit:continous'

            tasks << 'acceptance-tests:test'

            tasks << 'exportBacklog'
            tasks << 'aggregateTestReport'
            tasks << 'aggregateJDependReport'
            tasks << 'aggregateCoverageReport'
            tasks << 'aggregateFindbugsReport'

            doLast { println "Continous build passed, good work!" }
        }
        project.task("distributionPackage", type: GradleBuild, dependsOn: ['continousBuild']) { Task task ->
            group = 'Distribution'
            description = 'Distribution package for the whole thing including continous build.'
            buildFile = 'build.gradle'

            tasks << 'mybacklog:clean'
            tasks << 'perftence-concurrent:clean'
            tasks << 'perftence-bag:clean'
            tasks << 'perftence-api:clean'
            tasks << 'perftence-junit-utils:clean'
            tasks << 'perftence-classhelper:clean'
            tasks << 'perftence-linereader:clean'
            tasks << 'perftence-fileutil:clean'
            tasks << 'perftence:clean'
            tasks << 'responsecode-summaryappender:clean'
            tasks << 'perftence-junit:clean'

            tasks << 'mybacklog:release'
            tasks << 'perftence-concurrent:release'
            tasks << 'perftence-bag:release'
            tasks << 'perftence-api:release'
            tasks << 'perftence-junit-utils:release'
            tasks << 'perftence-classhelper:release'
            tasks << 'perftence-linereader:release'
            tasks << 'perftence-fileutil:release'
            tasks << 'perftence:release'
            tasks << 'responsecode-summaryappender:release'
            tasks << 'perftence-junit:release'
            tasks << 'makeDistributionPackage'

            doLast { println "Distribution package ready to be uploaded to the repository." }
        }
    }
}
