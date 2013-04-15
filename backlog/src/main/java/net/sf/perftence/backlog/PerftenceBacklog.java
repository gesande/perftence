package net.sf.perftence.backlog;

import net.sf.mybacklog.AbstractBacklogging;
import net.sf.mybacklog.Backlog;
import net.sf.mybacklog.BacklogFactory;

public class PerftenceBacklog extends AbstractBacklogging {
    private final BacklogFactory backlogFactory;

    public PerftenceBacklog(final BacklogFactory factory) {
        this.backlogFactory = factory;
    }

    public void show() {
        newBacklog()
                .title("Backlog for pertence test tool:")
                .done()
                .title("DONE:")
                .tasks(done(
                        "created mainentrypoint-example project with source example",
                        developmentSupport()),
                        done("add eclipse formatter settings under build project",
                                ide()),
                        done("additional build scripts for source jars",
                                developmentSupport()),
                        done("additional build scripts for distribution",
                                developmentSupport()),
                        done("publish to sourceforge", development()),
                        done("rename TPS -> throughput", refactoring()),
                        done("provide fluent based test examples with sources",
                                developmentSupport()),
                        done("provide agent based test examples with sources",
                                developmentSupport()),
                        done("provide distribution for test examples with sources",
                                developmentSupport()),
                        done("fix the source packages for 3rd party libs",
                                developmentSupport()),
                        done("removed unnecessary jars from the distribution packages",
                                developmentSupport()),
                        done("last second statistics for agent based tests",
                                feature()),
                        done("removed custom intermediate summary redundancy from intermediate summary builders",
                                refactoring()),
                        done("created perftence-junit-utils and responsecode-summaryappender projects",
                                refactoring()),
                        done("added some unit tests for the obvious ones",
                                development()),
                        done("distribution package for sourceforge",
                                developmentSupport()),
                        done("last second throughput graph for fluent based tests",
                                feature()),
                        done("added change log capabilities through svn diffing backlog",
                                infrastructure()),
                        done("added noInvocationGraph also for agent based test for overall statistics",
                                feature()),
                        done("enabled last second throughput graph writing also for agent based tests",
                                feature()),
                        done("agent based tests: category specific invocation reporters also respect noInvocationGraph() setting",
                                feature()),
                        done("agent based tests: optimize graph, no same graphs from overall to  category specific reports",
                                feature()),
                        done("agent based tests: Ability to turn off 'threads running current tasks' and task schedule differencies' measurements",
                                feature()),
                        done("acceptance-tests are also run when making a distribution package",
                                deployment()),
                        done("XYSeriesFactory in use, gave up using autoSort on XySeries",
                                feature()),
                        done("use emma to figure out where to put some more unit tests",
                                codeQuality()),
                        done("provided unit test for AdjustedFieldBuilder",
                                codeQuality()),
                        done("put perftence-bag into use all over the perftence",
                                refactoring()),
                        done("introduced perftence-junit module", refactoring()),
                        done("empty unit test for Statistics", codeQuality()),
                        done("first version of failures over test time -graph",
                                feature()),
                        done("last second throudisplayghput graph for agent based tests",
                                feature()),
                        done("new line-reader project", development()),
                        done("provide line-reader project in distribution package",
                                development()),
                        done("write unit tests for summary fields",
                                codeQuality()),
                        done("introduced perftence-api module",
                                developmentSupport()),
                        done("added perftence-api module to the continous and distribution builds",
                                deployment()),
                        done("removed runtime junit dependency from perftence, use it only as testCompile",
                                deployment()),
                        done("added dependency analysis with tattletale",
                                codeQuality()),
                        done("introduced perftence-fileutil module",
                                refactoring()),
                        done("added perftence-fileutil to the continous and distribution builds",
                                refactoring()),
                        done("provided unit tests for junit-utils module",
                                codeQuality()),
                        done("provided unit tests for classhelper module",
                                codeQuality()),
                        done("provided more unit tests for LatencyProvider",
                                codeQuality()),
                        done("removed static evil for EstimatedInvocations ",
                                refactoring()),
                        done("unit tests for PerformanceTestSetupPojo.noSetup()",
                                codeQuality()),
                        done("breakup stuff from build.gradle into separate .gradle files, e.g. libproject.gradle has only lib file repo",
                                build()),
                        done("simplified thread run engine for finding out threading issues, perftence-concurrent provides this now",
                                feature()),
                        done("fixed dist task, dist package didn't contain all zips",
                                build()),
                        done("gradle continousBuild", build()),
                        done("gradle aggregateReports", build()),
                        done("gradle distributionPackage", build()),
                        done("gradle main/testCodeAnalysis", build()),
                        done("gradle buildEclipseSettings", build()),
                        done("gradle newJavaProject", build()),
                        done("gradle newJavaLibProject", build()),
                        done("gradle svnRevision,svnStatus", build()),
                        done("gradle backlog:showBacklog", build()),
                        done("gradle backlog:hereIsSomethingTodo", build()),
                        done("gradle backlog:featuresWaiting", build()),
                        done("gradle svnfeaturesWaiting", build()),
                        done("gradle backlog:featuresWaiting", build()),
                        done("gradle addNewFilesToSvn,listFilesNotAddedToSvn",
                                build()),
                        done("gradle printChangeLog", build()),
                        done("gradle exportChangeLog", build()),
                        done("threadengine-api-example module",
                                developmentSupport()),
                        done("gradle testCodeDist,testCodeRelease",
                                developmentSupport()),
                        done("added median check for PerformanceRequirements",
                                feature()),
                        done("gradle createChangeLogScript", build()),
                        done("fixed gradle aggregateFindBugsReport auxclasspath",
                                build()),
                        done("gradle emma output test coverage after the tests",
                                build()),
                        done("gradle continousBuildWithoutAcceptanceTests",
                                build()),
                        done("backlog/build.gradle cleanup", build()),
                        done("new typeof gradle exportChangeLog", build()),
                        done("java based backlogging", backlog()),
                        done("simple-backlog module", backlog()),
                        done("chalkbox module", backlog()),
                        done("taking chalks out of DefaultBacklogAppender class",
                                backlog()),
                        done("gradle waitingForImplementation", backlog()),
                        done("replaced chalkbox with chalkbox-1.0.0", backlog()),
                        done("fixed eclipseJdt", ide()),
                        done("gradle licenseToCommit", build()),
                        done("removed build-stuff module and moved stuff to buildSrc",
                                build()),
                        done("gradle buildGradleForJavaProject and buildGradleForJavaLibProject used to create build.gradle for new projects.",
                                build()),
                        done("applied my-gradle-build.", build()))

                .inProgress()
                .title("IN PROGRESS:")
                .noTasks()

                .waiting()
                .title("WAITING:")
                .tasks(waiting("clean up obsolete gradle scripts", build()),
                        waiting("failures over test time, use DateAxis",
                                feature()),
                        waiting("provide success rate percentage for intermediate statistics",
                                feature()),
                        waiting("provide unit test for WritingFileFailed and other failure cases",
                                codeQuality()),
                        waiting("provide tools to create ResponseCodesPerSecond graph, see http://code.google.com/p/jmeter-plugins/wiki/ResponseCodesPerSecond for example",
                                feature()),
                        waiting("split fluent and agent stuff into separate projects",
                                refactoring()),
                        waiting("provide means to create test results in an intermediate format",
                                feature()),
                        waiting("provide means to create test report from an intermediate format",
                                feature()),
                        waiting("provide means to define also Throwable/Error with allow() mechanism",
                                feature()),
                        waiting("provide means to tell the Executable  also Throwable/Error with allow() mechanism",
                                feature()),
                        waiting("upload test example distribution packages to project files (i.e. to source forge)",
                                developmentSupport()),
                        waiting("one intermediate summary statistics appender",
                                refactoring()),
                        waiting("latency frequencies -> ability to set the range for the graph e.g. using 99% percentile",
                                feature()),
                        waiting("statistics enhancement: define a latency rate",
                                feature()),
                        waiting("statistics enhancement: print out statistics for latencies over the defined rate i.e. statistics over statistics",
                                feature()),
                        waiting("study changing public interface DatasetAdapter GRAPHDATA to DatasetAdapter GRAPHDATA, CATEGORY",
                                refactoring()),
                        waiting("ability to define the TPS is used defining it before running the test, e.g. running at 500 TPS max when the test is running",
                                feature()))

                .show();
    }

    @Override
    protected Backlog newBacklog() {
        return backlogFactory().newBacklog();
    }

    private BacklogFactory backlogFactory() {
        return this.backlogFactory;
    }

    private static PerftenceTag build() {
        return PerftenceTag.build;
    }

    private static PerftenceTag codeQuality() {
        return PerftenceTag.codeQuality;
    }

    private static PerftenceTag deployment() {
        return PerftenceTag.deployment;
    }

    private static PerftenceTag infrastructure() {
        return PerftenceTag.infrastructure;
    }

    private static PerftenceTag feature() {
        return PerftenceTag.feature;
    }

    private static PerftenceTag refactoring() {
        return PerftenceTag.refactoring;
    }

    private static PerftenceTag development() {
        return PerftenceTag.development;
    }

    private static PerftenceTag ide() {
        return PerftenceTag.ide;
    }

    private static PerftenceTag developmentSupport() {
        return PerftenceTag.developmentSupport;
    }

    private static PerftenceTag backlog() {
        return PerftenceTag.backlog;
    }
}
