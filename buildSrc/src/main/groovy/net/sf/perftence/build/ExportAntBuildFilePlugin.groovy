package net.sf.perftence.build

import net.sf.mygradlebuild.tasks.ExportAntBuildFileTask

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExportAntBuildFilePlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        project.task('exportAntBuildFile', type: ExportAntBuildFileTask ) { ExportAntBuildFileTask task ->
            group = 'Build'
            description = 'Creates a ant build file for the project which contains the most important targets.'
            task.parent = "buildSrc"
            task.buildFilename= "build.xml"
            task.defaultTarget= "continousBuild"
            task.targets = [
                "distributionPackage",
                "eclipseSettings",
                "exportAntBuildFile",
                "tasks"
            ]
        }
    }
}
