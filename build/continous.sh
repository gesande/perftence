#!/bin/bash
set -eu

gradle clean
gradle --stacktrace perftence-concurrent:continous perftence-bag:continous perftence-linereader:continous perftence-fileutil:continous perftence-junit-utils:continous perftence-classhelper:continous perftence:continous perftence-api:continous responsecode-summaryappender:continous perftence-junit:continous acceptance-tests:test
gradle aggregateTestReport
build/emma/report.sh
build/findbugs/report.sh
build/jdepend/report.sh

