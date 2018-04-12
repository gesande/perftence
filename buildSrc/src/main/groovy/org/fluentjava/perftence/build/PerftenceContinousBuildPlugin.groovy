package org.fluentjava.perftence.build;

import org.gradle.api.Plugin
import org.gradle.api.Project
import net.sf.mygradlebuild.plugins.JavaProjectArtifactPlugin
import net.sf.mygradlebuild.plugins.FindbugsWithHtmlReports
import net.sf.mygradlebuild.plugins.ForkPmdSettings
import net.sf.mygradlebuild.plugins.JDependWithXmlReports
import net.sf.mygradlebuild.plugins.CodeAnalysis
import net.sf.mygradlebuild.plugins.ContinousPlugin
import net.sf.mygradlebuild.plugins.ProjectVersion

public class PerftenceContinousBuildPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.plugins.apply(JavaProjectArtifactPlugin)
		project.plugins.apply(FindbugsWithHtmlReports)
		project.plugins.apply(ForkPmdSettings)
		project.plugins.apply(JDependWithXmlReports)
		project.plugins.apply(CodeAnalysis)
		project.plugins.apply(ContinousPlugin)
		project.plugins.apply(ProjectVersion)
	}
}
