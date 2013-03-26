package net.sf.perftence.build;

import java.io.File;

import org.junit.Test;

public class BuildGradleGeneratorTest {

    @SuppressWarnings("static-method")
    @Test
    public void forJavaProject() {
        final GradleBuildFileGenerator generator = new GradleBuildFileGenerator();
        final String project = "javaproject";
        generator.forJavaProject(new File("target", project), project);
        // TODO: assert contents
    }

    @SuppressWarnings("static-method")
    @Test
    public void forJavaLibProject() {
        final GradleBuildFileGenerator generator = new GradleBuildFileGenerator();
        final String project = "lib-1.0.0";
        generator.forJavaLibProject(new File("target", project), project);
        // TODO: assert contents
    }

}
