#!/bin/bash
set -eu

continous-build() {
  local PROJECT=$1
  gradle --info $PROJECT:clean $PROJECT:test $PROJECT:copy-pmd-settings $PROJECT:findbugsMain $PROJECT:pmdMain $PROJECT:jdependMain
}
start_time=`date +%s`
continous-build perftence
continous-build responsecode-summaryappender
continous-build perftence-junit-utils
continous-build perftence-classhelper
continous-build perftence-linereader
end_time=`date +%s`
echo
echo TOTAL BUILD TIME: `expr $end_time - $start_time` s.
