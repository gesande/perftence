#!/bin/bash
set -eu

gradle clean perftence-concurrent:continous perftence-bag:continous perftence-linereader:continous perftence-fileutil:continous perftence-junit-utils:continous perftence-classhelper:continous perftence:continous perftence-api:continous responsecode-summaryappender:continous perftence-junit:continous
gradle acceptance-tests:test
build/emma/report.sh
build/findbugs/report.sh
build/jdepend/report.sh
