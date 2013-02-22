#!/bin/bash
set -e

tar-file() {
  local REVISION=$(svnversion)
  local VERSION=$(gradle perftence:show-version | grep -A1 :perftence:show-version | tail -1)
  local ARTIFACT_VERSION="$VERSION-R$REVISION"
  local ARTIFACT_FINAL="tmp/artifact-$ARTIFACT_VERSION/perftence-distribution-$ARTIFACT_VERSION.tar"

  rm -rf tmp/artifact-$ARTIFACT_VERSION
  rm -rf tmp/distribution-$ARTIFACT_VERSION

  mkdir -p tmp/artifact-$ARTIFACT_VERSION

  tar -cvf $ARTIFACT_FINAL distribution

  mv distribution tmp/distribution-$ARTIFACT_VERSION
  
  echo "Distribution package can be found from $(in-green file://$(realpath $ARTIFACT_FINAL))"
  echo "Exploded path for the distribution is here: $(in-green file://$(realpath tmp/distribution-$ARTIFACT_VERSION))"
}

in-green() {
  local WHAT=$1
  echo -e "\033[32m$WHAT\e[m"
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
  gradle clean perftence-concurrent:continous perftence-fileutil:continous perftence-bag:continous perftence:continous responsecode-summaryappender:continous perftence-api:continous perftence-junit-utils:continous perftence-classhelper:continous perftence-linereader:continous perftence-junit:continous acceptance-tests:test
  gradle clean perftence-concurrent:release perftence-bag:release perftence-api:release perftence-junit-utils:release perftence-classhelper:release perftence-linereader:release perftence-fileutil:release perftence:release responsecode-summaryappender:release perftence-junit:release
}

build-packages
prepare-distribution
copy-to-distribution
tar-file
