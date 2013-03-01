#!/bin/bash
set -eu

generate-emma-html-report() {
  echo "Creating overall emma coverage report..."
  local FILES=$(find -name *.emma)
  local IN=$(for file in $FILES; do echo -n "-in $file ";done)

  local SOURCEPATH=$(for source in $(ls **/src/main/java); do echo -n "-sourcepath $source ";done)

  java -cp tools/emma-2.1.5320-lib/emma.jar emma report -report html $SOURCEPATH $IN -Dreport.out.file=tmp/emma/coverage.html
  echo "Overall emma coverage report: file://$(realpath tmp/emma/coverage.html)"
}

generate-emma-html-report
