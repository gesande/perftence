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
  gradle clean perftence:test responsecode-summaryappender:test acceptance-tests:test perftence-junit-utils:test perftence-classhelper:test perftence:dist perftence:sourcesJar responsecode-summaryappender:dist responsecode-summaryappender:sourcesJar perftence-junit-utils:dist perftence-junit-utils:sourcesJar perftence-classhelper:dist perftence-classhelper:sourcesJar
}

build-packages
prepare-distribution
copy-to-distribution
tar-file
clean-distribution
