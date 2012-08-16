#!/bin/bash
set -eu

distribution-with-sources() {
  local PROJECT=$1
  gradle clean $PROJECT:build $PROJECT:dist $PROJECT:sourcesJar
}

distribution-with-sources "mainentrypoint-example"
