

package org.fluentjava.perftence

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec

class PerftenceBacklogBuildPlugin implements Plugin<Project>{

	@Override
	public void apply(final Project project) {
		project.task("showBacklog",dependsOn: "classes") { Task task ->
			group = 'Backlog'
			description= 'Shows the backlog.'
			doLast {
				javaexec {
					main = 'org.fluentjava.perftence.backlog.PerftenceBacklogMain'
					classpath  sourceSets.main.output.classesDir
					classpath configurations.runtime
				}
			}
		}

		project.task("exportBacklog", dependsOn: "classes") { Task task ->
			group = 'Backlog'
			description= 'Exports the backlog to backlog.txt.'
			doLast {
				def OutputStream backlog = new FileOutputStream("backlog.txt")
				javaexec {
					main = 'org.fluentjava.perftence.backlog.PerftenceBacklogMain'
					classpath  sourceSets.main.output.classesDir
					classpath configurations.runtime
					standardOutput = backlog
				}
			}
		}

		project.task("waitingForImplementation",dependsOn: "classes") { Task task ->
			group = 'Backlog'
			description= 'Shows all stuff waiting for implementation.'
			doLast {
				javaexec {
					main = 'org.fluentjava.perftence.backlog.BacklogWaitingForImplementation'
					classpath  sourceSets.main.output.classesDir
					classpath configurations.runtime
				}
			}
		}

		project.task("featuresWaiting" ,dependsOn: "classes") { Task task ->
			group = 'Backlog'
			description= 'Shows all features waiting for implementation.'
			doLast {
				javaexec {
					main = 'org.fluentjava.perftence.backlog.BacklogWaitingForImplementation'
					args 'feature'
					classpath  sourceSets.main.output.classesDir
					classpath configurations.runtime
				}
			}
		}

		project.task("printChangeLog", type: Exec) { Exec task ->
			group = 'Backlog'
			description= 'Change log for backlog.'
			task.executable = "bash"
			task.args "-c", changeLogArgs(task)
		}

		project.task("copyStdLib") { Task task ->
			group ='Backlog'
			description = "Copies stdout-lib.sh for changelog scripts to ${distributionDir}."
			doLast {
				copy {
					from "${task.project.projectDir}"
					include 'stdout-lib.sh'
					into distributionDir
				}
			}
		}

		project.task("createChangeLogScript", dependsOn: "copyStdLib") { Task task ->
			group ='Backlog'
			description = 'Create a shell script for a specific changelog.'

			doLast {
				def String scriptFile = "${distributionDir}/changelog-from-${fromRevision}-to-${toRevision}.sh"
				exec {
					executable = 'bash'
					args "-c"
					args "echo '#!/bin/bash' > ${scriptFile}"
				}
				exec {
					executable ='bash'
					args "-c"
					args "echo 'set -eu' >> ${scriptFile}"
				}
				exec {
					executable ='bash'
					args "-c"
					args "echo 'HERE=\$(dirname \$0)' >> ${scriptFile}"
				}
				exec {
					executable ='bash'
					args "-c"
					args "echo '. \$HERE/stdout-lib.sh' >> ${scriptFile}"
				}
				exec {
					executable = 'bash'
					args "-c"
					args changeLogArgs(task) +" | sed 's/+//' " +" >> ${scriptFile}"
				}
				exec {
					executable = 'chmod'
					args "a+x"
					args "${scriptFile}"
				}
				println "Get the change log by running the script from ${scriptFile}"
			}
		}

		project.task("exportChangeLog", dependsOn: "createChangeLogScript")  { Task task ->
			group = 'Backlog'
			description= 'Exports changelog.'
			doLast {
				exec {
					executable = "bash"
					args "-c"
					args generatedScriptFile() + replaceBacklogPlus()
				}
			}
		}

		project.task("exportChangeLogTo", dependsOn: "createChangeLogScript")  { Task task ->
			group = 'Backlog'
			description= 'Exports changelog to given file.'
			doLast{
				exec {
					executable = "bash"
					args "-c"
					args generatedScriptFile() + replaceBacklogPlus() + " >> $exportTo"
				}
			}
		}
	}

	private String generatedScriptFile() {
		return "${distributionDir}/changelog-from-${fromRevision}-to-${toRevision}.sh"
	}

	private String replaceBacklogPlus() {
		return " | sed 's/ +++ //' | sed 's/ +++ /|/'"
	}

	private String changeLogArgs(Task task) {
		def String args = "svn diff -r ${task.project.properties.fromRevision}:${task.project.properties.toRevision} ${task.project.projectDir}/show.sh | grep task-done | grep '+'"
		return args
	}
}
