#!/bin/bash
set -eu
new-java-project() {
  local PROJECT=$1
  gradle $PROJECT:create-dirs
  echo "$PROJECT directory structure done."
  svn add $PROJECT
  ./build/svn/ignore.sh $PROJECT
  echo "Project put into svn"
  gradle $PROJECT:copyPmdSettings $PROJECT:eclipse
  echo "Eclipse settings done for $PROJECT"
  echo "Now import (without copying) the project into eclipse"
}

new-java-project $1
