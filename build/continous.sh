#!/bin/bash
set -eu

continous-build() {
  local PROJECT=$1
  gradle --info $PROJECT:clean $PROJECT:test $PROJECT:copy-pmd-settings $PROJECT:findbugsMain $PROJECT:pmdMain $PROJECT:jdependMain
}

continous-build perftence
continous-build responsecode-summaryappender
continous-build perftence-junit-utils
continous-build perftence-classhelper

