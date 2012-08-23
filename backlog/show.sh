#!/bin/bash
set -eu
HERE=$(dirname $0)
. $HERE/stdout-lib.sh

title

done-title
task-done "created mainentrypoint-example project with source example" "#development-support" 
task-done "add eclipse formatter settings under build project" "#ide"
task-done "additional build scripts for source jars" "#development-support"
task-done "additional build scripts for distribution" "#development-support"
task-done "publish to sourceforge" "#development"

in-progress-title
in-progress "provide fluent based test examples with sources" "#development-support" 

waiting-title
waiting "rename TPS -> throughput" "#refactoring"
waiting "provide agent based test examples with sources" "#development-support" 
waiting "provide distribution for test examples with sources" "#development-support"
waiting "upload test example distribution packages to project files (i.e. to source forge)" "#development-support"
waiting "fix the source packages for 3rd party lib" "#development-support"
waiting "move build.gradle & settings.gradle under build project" "#build"
waiting "make shell script library for build tasks" "#build" 
waiting "split fluent and agent stuff into separate projects" "#refactoring"
intended-comment "may reflect to need of: separate 'perftence' and 'report' stuff" 
waiting "write unit tests for summary fields" "#tests"
waiting "one intermediate summary statistics appender" "#refactoring"
intended-comment "define each summary item as a separate 'plugin' which is registered to one implementation --> one summary builder for agent and executable -based tests, each define their own summary 'plugin'"
waiting "latency frequencies -> ability to set the range for the graph e.g. using 99% percentile" "#feature" 
waiting "statistics enhancement" "#feature"
intended-comment "define a latency rate"
intended-comment "print out statistics for latencies over the defined rate i.e. statistics over statistics"
waiting "change" "#refactoring"
intended-comment "public interface DatasetAdapter GRAPHDATA to DatasetAdapter GRAPHDATA, CATEGORY"
waiting "ability to define the TPS is used defining it before running the test" "#feature"
intended-comment "e.g. running at 500 TPS max when the test is running"
