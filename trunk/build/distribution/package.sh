#!/bin/bash
set -e

tar-file() {
  local REVISION=$(svnversion)
  local VERSION=$(gradle perftence:show-version | grep -A1 :perftence:show-version | tail -1)
  local ARTIFACT=perftence-distribution-$VERSION-R$REVISION.tar
  tar -cvf $ARTIFACT distribution
}

copy-to-distribution() {
  cp -a **/build/distributions/*.zip distribution
  cp -a **/build/libs/*sources.jar distribution/sources
  cp -a COPYING distribution
}

prepare-distribution() {
  clean-distribution
  mkdir -p distribution/sources
}

clean-distribution() {
  rm -rf distribution
}

build-packages() {
  gradle clean perftence-fileutil:test perftence-bag:test perftence:test responsecode-summaryappender:test perftence-api:test perftence-junit-utils:test perftence-classhelper:test perftence-linereader:test perftence-junit:test acceptance-tests:test
  gradle clean perftence-bag:dist perftence-bag:sourcesJar perftence-api:dist perftence-api:sourcesJar perftence-junit-utils:dist perftence-junit-utils:sourcesJar perftence-classhelper:dist perftence-classhelper:sourcesJar perftence-linereader:dist perftence-linereader:sourcesJar perftence-fileutil:dist perftence-fileutil:sourcesJar perftence:dist perftence:sourcesJar responsecode-summaryappender:dist responsecode-summaryappender:sourcesJar perftence-junit:dist perftence-junit:sourcesJar
}

start_time=`date +%s`
build-packages
prepare-distribution
copy-to-distribution
tar-file
clean-distribution
end_time=`date +%s`
echo
echo TOTAL BUILD TIME: `expr $end_time - $start_time` s.
