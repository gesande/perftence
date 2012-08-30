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
task-done "created perftence-junit-utils and responsecode-summaryappender projects"

in-progress-title

waiting-title

waiting "last second throughput graph" $(tag feature)
waiting "provide means to define also Throwable/Error with allow() mechanism" $(tag development-support)
waiting "provide means to tell the Executable  also Throwable/Error with allow() mechanism" $(tag development-support)
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