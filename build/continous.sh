#!/bin/bash
set -eu

continous-build() {
  local PROJECT=$1
  gradle --info $PROJECT:test $PROJECT:copy-pmd-settings $PROJECT:findbugsMain $PROJECT:pmdMain $PROJECT:jdependMain
}
start_time=`date +%s`
gradle --info clean
continous-build perftence-bag
continous-build perftence-linereader
continous-build perftence-fileutil
continous-build perftence-junit-utils
continous-build perftence-classhelper
continous-build perftence
continous-build perftence-api
continous-build responsecode-summaryappender
continous-build perftence-junit
end_time=`date +%s`
echo
echo TOTAL BUILD TIME: `expr $end_time - $start_time` s.
