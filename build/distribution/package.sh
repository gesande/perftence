#!/bin/bash
set -e


tar-file() {
  local REV=$(svnversion)
  local ARTIFACT=perftence-distribution-$REV.tar
  rm -rf distribution && mkdir -p distribution/sources
  cp -a **/build/distributions/*.zip distribution
  cp -a **/build/libs/*sources.jar distribution/sources

  echo "ARTIFACT =$ARTIFACT"
  tar -cvf $ARTIFACT distribution
  rm -rf distribution
}

build-packages() {
  gradle clean perftence:dist perftence:sourcesJar responsecode-summaryappender:dist responsecode-summaryappender:sourcesJar perftence-junit-utils:dist perftence-junit-utils:sourcesJar
}

build-packages
tar-file
