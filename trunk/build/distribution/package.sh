#!/bin/bash
set -e


tar-file() {
  local REV=$(svnversion)
  local ARTIFACT=perftence-distribution-$REV.tar
  echo "ARTIFACT =$ARTIFACT"
  tar -cvf $ARTIFACT **/build/distributions/*.zip **/build/libs/*sources.jar
}

build-packages() {
  gradle clean perftence:dist perftence:sourcesJar responsecode-summaryappender:dist responsecode-summaryappender:sourcesJar perftence-junit-utils:dist perftence-junit-utils:sourcesJar
}

build-packages
tar-file
