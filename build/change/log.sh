#/bin/bash
set -eu

change-log() {
  local FROM=$1
  local TO=$2
  svn diff -r $FROM:$TO backlog/show.sh | grep task-done | grep "+"
}

change-log $1 $2


