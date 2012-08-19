#!/bin/bash
set -eu

title() {
  echo "Backlog for pertence test tool:"
  echo 
}

task-done() {
  local TASK=$1
  local TAGS=$2
  echo "+++ $TASK +++ $TAGS"
}

done-title() {
  echo 
  echo "DONE:"
  echo
}

in-progress-title() {
  echo
  echo "IN PROGRESS:"
  echo
}

in-progress() {
  local TASK=$1
  local TAGS=$2
  echo "- $TASK $TAGS -"
}

waiting-title() {
  echo
  echo "WAITING:"
  echo
}

intended-comment() {
  local COMMENT=$1
  echo "  * $COMMENT"
}

waiting() {
  local TASK=$1
  local TAGS=$2
  echo "--- $TASK --- $TAGS"
}

