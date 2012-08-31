#!/bin/bash
set -eu

distribution-with-sources() {
  local PROJECT=$1
  gradle clean $PROJECT:build $PROJECT:dist $PROJECT:sourcesJar
}

make-distribution-artifact() {

  local PROJECT=$1
  local VERSION=$(gradle $PROJECT:show-version | grep -A1 "show-version" | sed -n 2p)
  cd $PROJECT/build/
  rm -rf dist
  mkdir dist
  echo "Copying all libs jars to dist" 
  cp libs/*.jar dist
  echo "Copying all distributions to dist" 
  cp distributions/*.zip dist
  local ARTIFACT=$PROJECT-$VERSION-dist.tar
  echo "Making the tar distribution artifact $ARTIFACT" 
  tar -cf $ARTIFACT dist
  echo "DONE."
  echo "Distribution artifact $ARTIFACT can be found from $(pwd $ARTIFACT)" 
}

build-distribution-artifact() {
  local PROJECT=$1
  distribution-with-sources $PROJECT
  make-distribution-artifact $PROJECT
}
