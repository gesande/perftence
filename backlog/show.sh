#!/bin/bash
set -eu
HERE=$(dirname $0)
. $HERE/stdout-lib.sh
clear 

title

done-title
task-done "created mainentrypoint-example project with source example" $(tag development-support)
task-done "add eclipse formatter settings under build project" $(tag ide)
task-done "additional build scripts for source jars" $(tag development-support)
task-done "additional build scripts for distribution" $(tag development-support)
task-done "publish to sourceforge" $(tag development)
task-done "rename TPS -> throughput" $(tag refactoring)
task-done "provide fluent based test examples with sources" $(tag development-support)
task-done "provide agent based test examples with sources" $(tag development-support)
task-done "provide distribution for test examples with sources" $(tag development-support)
task-done "fix the source packages for 3rd party libs" $(tag development-support)
task-done "removed unnecessary jars from the distribution packages" $(tag development-support)
task-done "last second statistics for agent based tests" $(tag feature)
task-done "removed custom intermediate summary redundancy from intermediate summary builders" $(tag refactoring)
task-done "created perftence-junit-utils and responsecode-summaryappender projects" $(tag refactoring)
task-done "added some unit tests for the obvious ones" $(tag development)
task-done "distribution package for sourceforge" $(tag development-support)
task-done "last second throughput graph for fluent based tests" $(tag feature)
task-done "added change log capabilities through svn diffing backlog" $(tag infrastructure)
task-done "added noInvocationGraph also for agent based test for overall statistics" $(tag feature)
task-done "enabled last second throughput graph writing also for agent based tests" $(tag feature)
task-done "agent based tests: category specific invocation reporters also respect noInvocationGraph() setting" $(tag feature)
task-done "agent based tests: optimize graph, no same graphs from overall to  category specific reports" $(tag feature)
task-done "agent based tests: Ability to turn off 'threads running current tasks' and task schedule differencies' measurements" $(tag feature)
task-done "acceptance-tests are also run when making a distribution package"

in-progress-title

waiting-title

waiting "provide means to create test results in an intermediate format" $(tag feature)
intended-comment "two-phased construction -> first tests -> produce results to an intermediate format"
waiting "provide means to create test report from an intermediate format" $(tag feature)
intended-comment "second phase -> create test reports from intermediate format"
waiting "new line-reader project" $(tag development)
waiting "provide line-reader project in distribution package" $(tag development)
waiting "provide success rate percentage for intermediate statistics" $(tag feature)
waiting "provide tools to create ResponseCodesPerSecond graph, see http://code.google.com/p/jmeter-plugins/wiki/ResponseCodesPerSecond for example" $(tag feature)
waiting "last second throughput graph for agent based tests" $(tag feature)
waiting "provide means to define also Throwable/Error with allow() mechanism" $(tag feature)
waiting "provide means to tell the Executable  also Throwable/Error with allow() mechanism" $(tag feature)
waiting "provide template pom.xml for developers" $(tag development-support)
waiting "upload test example distribution packages to project files (i.e. to source forge)" $(tag development-support)
waiting "move build.gradle & settings.gradle under build project" $(tag build)
waiting "make shell script library for build tasks" $(tag build)
waiting "split fluent and agent stuff into separate projects" $(tag refactoring)
intended-comment "may reflect to need of: separate 'perftence' and 'report' stuff" 
waiting "write unit tests for summary fields" $(tag tests)
waiting "one intermediate summary statistics appender" $(tag refactoring)
intended-comment "define each summary item as a separate 'plugin' which is registered to one implementation --> one summary builder for agent and executable -based tests, each define their own summary 'plugin'"
waiting "latency frequencies -> ability to set the range for the graph e.g. using 99% percentile" $(tag feature)
waiting "statistics enhancement" $(tag feature)
intended-comment "define a latency rate"
intended-comment "print out statistics for latencies over the defined rate i.e. statistics over statistics"
waiting "change" $(tag refactoring)
intended-comment "public interface DatasetAdapter GRAPHDATA to DatasetAdapter GRAPHDATA, CATEGORY"
waiting "ability to define the TPS is used defining it before running the test" $(tag feature)
intended-comment "e.g. running at 500 TPS max when the test is running"
waiting "simplified thread run engine for finding out threading issues" $(tag feature)
