#!/bin/bash
set -eu

distribution-with-sources() {
  local PROJECT=$1
  gradle clean $PROJECT:build $PROJECT:dist $PROJECT:sourcesJar
}

make-dist-tar() {
  local PROJECT=$1
  cd $PROJECT/build/
  rm -rf dist
  mkdir dist
  cp libs/*.jar dist
  cp distributions/*.zip dist
  tar -cf $PROJECT-dist.tar dist
}

distribution-with-sources "fluent-based-example"
make-dist-tar "fluent-based-example"
