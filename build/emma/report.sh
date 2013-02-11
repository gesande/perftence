#!/bin/bash
set -eu

generate-emma-html-report() {
  echo "Creating overall emma coverage"
  local FILES=$(find -name *.emma)
  local IN=$(for file in $FILES; do echo -n "-in $file ";done)
  java -cp tools/emma-2.1.5320-lib/emma.jar emma report -r html $IN -Dreport.out.file=tmp/emma/coverage.html
  echo "Overall emma coverage report: from file://$(realpath tmp/emma/coverage.html)"
}

generate-emma-html-report
