#!/bin/bash
set -eu

gradle --stacktrace clean perftence-concurrent:continous perftence-bag:continous perftence-linereader:continous perftence-fileutil:continous perftence-junit-utils:continous perftence-classhelper:continous perftence:continous perftence-api:continous responsecode-summaryappender:continous perftence-junit:continous acceptance-tests:test aggregateTestReport aggregateJDependReport aggregateEmmaReport aggregateFindbugsReport
