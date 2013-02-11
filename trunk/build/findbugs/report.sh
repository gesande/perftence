#!/bin/bash
set -eu
mkdir -p tmp/findbugs
echo "Creating overall findbugs report..."
tools/findbugs-2.0.2/bin/findbugs -javahome $JAVA_HOME/jre -textui -html -output tmp/findbugs/index.html -onlyAnalyze net.sf.perftence.- -auxclasspath **/lib/*.jar $JAVA_HOME/jre/lib/*.jar **/build/libs/*.jar
echo "Overall findbugs report: file://$(realpath tmp/findbugs/index.html)"

