#!/bin/bash
set -e

tar-file() {
  local REVISION=$(svnversion)
  local VERSION=$(gradle perftence:show-version | grep -A1 :perftence:show-version | tail -1)
  local ARTIFACT=perftence-distribution-$VERSION-R$REVISION.tar
  rm -rf tmp/artifact
  mkdir -p tmp/artifact
  tar -cvf tmp/artifact/$ARTIFACT distribution
  mv distribution tmp/distribution
  echo "Distribution package can be found from file://$(realpath tmp/artifact/$ARTIFACT)"
}

copy-to-distribution() {
  cp -a **/build/distributions/*.zip distribution
  cp -a **/build/libs/*sources.jar distribution/sources
  cp -a COPYING distribution
}

prepare-distribution() {
  mkdir -p distribution/sources
}

build-packages() {
  gradle clean perftence-fileutil:test perftence-bag:test perftence:test responsecode-summaryappender:test perftence-api:test perftence-junit-utils:test perftence-classhelper:test perftence-linereader:test perftence-junit:test acceptance-tests:test
  gradle clean perftence-bag:dist perftence-bag:sourcesJar perftence-api:dist perftence-api:sourcesJar perftence-junit-utils:dist perftence-junit-utils:sourcesJar perftence-classhelper:dist perftence-classhelper:sourcesJar perftence-linereader:dist perftence-linereader:sourcesJar perftence-fileutil:dist perftence-fileutil:sourcesJar perftence:dist perftence:sourcesJar responsecode-summaryappender:dist responsecode-summaryappender:sourcesJar perftence-junit:dist perftence-junit:sourcesJar
}

build-packages
prepare-distribution
copy-to-distribution
tar-file
