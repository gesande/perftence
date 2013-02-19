#!/bin/bash
set -eu

continous-build() {
  local PROJECT=$1
  gradle --info $PROJECT:continous
}
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
build/emma/report.sh
build/findbugs/report.sh
build/jdepend/report.sh
