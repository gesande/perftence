package net.sf.perftence.build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GradleBuildFileGenerator {

    @SuppressWarnings("static-method")
    public void forJavaProject(final File file, final String name) {
        makingSureParentDirectoryExists(file, name);
        writeGradleBuildFile(file, javaProjectContents(name));
    }

    @SuppressWarnings("static-method")
    public void forJavaLibProject(final File file, final String name) {
        makingSureParentDirectoryExists(file, name);
        writeGradleBuildFile(file, javaLibProjectContents(name));

    }

    private static StringBuilder javaProjectContents(final String name) {
        final StringBuilder sb = new StringBuilder("project(':").append(name)
                .append("') {").append("\n")

                .append(tab()).append("apply from: \"$emmaPlugin\"")
                .append("\n")

                .append(tab()).append("apply from: \"$distributionPlugin\"")
                .append("\n").

                append("}");
        return sb;
    }

    private static StringBuilder javaLibProjectContents(final String name) {
        final StringBuilder sb = new StringBuilder("project(':").append(name)
                .append("') { prj ->").append("\n")

                .append(tab()).append("apply from: \"$libraryPlugin\"")
                .append("\n")

                .append(tab()).append("prj.ext.library='").append(name)
                .append(".jar'\n")

                .append(tab()).append("prj.ext.librarySources='").append(name)
                .append("-sources.jar'\n")

                .append("}");
        return sb;
    }

    private static String tab() {
        return "    ";
    }

    private static BufferedWriter newBufferedWriter(final File buildFile) {
        try {
            return new BufferedWriter(new FileWriter(buildFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void makingSureParentDirectoryExists(final File file,
            final String name) {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException(
                        "Not able to create the parent directories for '"
                                + name + "'! Need to exit.");
            }
        }
    }

    private static void writeGradleBuildFile(final File file,
            final StringBuilder contents) {
        writeGradleBuildFile(newBufferedWriter(newGradleBuildFile(file)),
                contents);
    }

    private static File newGradleBuildFile(final File file) {
        return new File(file, "build.gradle");
    }

    private static void writeGradleBuildFile(final BufferedWriter out,
            final StringBuilder javaProjectContents) {
        try {
            out.write(javaProjectContents.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
